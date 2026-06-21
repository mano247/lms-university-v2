package com.lmsuniversity.examperiod;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lmsuniversity.course.Course;
import com.lmsuniversity.examattempt.ExamAttempt;
import com.lmsuniversity.examattempt.ExamAttemptRepository;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.user.StudentAffairsOffice;
import com.lmsuniversity.user.StudentAffairsOfficeRepository;
import com.lmsuniversity.user.StudentRepository;

@Service
public class ExamPeriodTermService {

	@Autowired
	private ExamPeriodTermRepository repository;

	@Autowired
	private ExamPeriodRepository examPeriodRepository;

	@Autowired
	private ExamAttemptRepository examAttemptRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentAffairsOfficeRepository studentAffairsOfficeRepository;

	@Autowired
	private ExamPeriodTermMapper mapper;

	public Optional<ExamPeriodTerm> findOne(Long id) {
		return repository.findById(id);
	}

	public boolean canManage(ExamPeriodTerm term, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		return canManageCourse(term.getExamPeriod().getCourse(), requestingUserId, isAdmin, isStudentAffairs);
	}

	public long countRegistered(Long termId) {
		return examAttemptRepository.countByExamPeriodTermId(termId);
	}

	public ExamPeriodTerm create(Long examPeriodId, ExamPeriodTermCreateDto dto, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		ExamPeriod examPeriod = examPeriodRepository.findById(examPeriodId)
				.orElseThrow(() -> new IllegalArgumentException("Exam period not found: " + examPeriodId));
		if (!canManageCourse(examPeriod.getCourse(), requestingUserId, isAdmin, isStudentAffairs)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"You are not allowed to add a term to this exam period.");
		}
		ExamPeriodTerm term = mapper.toEntity(dto);
		term.setExamPeriod(examPeriod);
		return repository.save(term);
	}

	public ExamPeriodTerm update(Long id, ExamPeriodTermUpdateDto dto, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		ExamPeriodTerm term = repository.findById(id).orElse(null);
		if (term == null || !canManageCourse(term.getExamPeriod().getCourse(), requestingUserId, isAdmin, isStudentAffairs)) {
			return null;
		}
		mapper.updateEntityFromDto(dto, term);
		return repository.save(term);
	}

	public boolean delete(Long id, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		ExamPeriodTerm term = repository.findById(id).orElse(null);
		if (term == null || !canManageCourse(term.getExamPeriod().getCourse(), requestingUserId, isAdmin, isStudentAffairs)) {
			return false;
		}
		if (examAttemptRepository.existsByExamPeriodTermId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete this exam term: students have already registered for it.");
		}
		repository.deleteById(id);
		return true;
	}

	public ExamAttempt register(Long termId, Long studentId) {
		ExamPeriodTerm term = repository.findById(termId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam period term not found."));
		ExamPeriod period = term.getExamPeriod();

		LocalDate today = LocalDate.now();
		if (today.isBefore(period.getStartDate()) || today.isAfter(period.getEndDate())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"This exam period is not currently open for registration.");
		}

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
		if (!period.getCourse().getStudents().contains(student)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not enrolled in this course.");
		}
		if (examAttemptRepository.existsByExamPeriodTermIdAndStudentId(termId, studentId)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "You are already registered for this exam term.");
		}
		if (examAttemptRepository.countByExamPeriodTermId(termId) >= term.getMaxStudents()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "This exam term is full.");
		}

		ExamAttempt attempt = ExamAttempt.builder()
				.course(period.getCourse())
				.student(student)
				.teacher(period.getCourse().getTeacher())
				.examPeriodTerm(term)
				.points(0)
				.finalGrade(0)
				.build();
		return examAttemptRepository.save(attempt);
	}

	private boolean canManageCourse(Course course, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		if (isAdmin) {
			return true;
		}
		if (isStudentAffairs) {
			StudentAffairsOffice office = studentAffairsOfficeRepository.findById(requestingUserId).orElse(null);
			Faculty courseFaculty = course.getStudyProgram() != null ? course.getStudyProgram().getFaculty() : null;
			return office != null && office.getFaculty() != null && courseFaculty != null
					&& office.getFaculty().getId().equals(courseFaculty.getId());
		}
		return course.getTeacher().getId().equals(requestingUserId);
	}
}
