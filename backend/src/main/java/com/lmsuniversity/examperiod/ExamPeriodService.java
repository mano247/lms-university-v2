package com.lmsuniversity.examperiod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import com.lmsuniversity.course.Course;
import com.lmsuniversity.course.CourseRepository;
import com.lmsuniversity.examattempt.ExamAttemptRepository;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.user.RegisteredUser;
import com.lmsuniversity.user.StudentAffairsOffice;
import com.lmsuniversity.user.StudentAffairsOfficeRepository;

@Service
public class ExamPeriodService {

	@Autowired
	private ExamPeriodRepository repository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private ExamAttemptRepository examAttemptRepository;

	@Autowired
	private StudentAffairsOfficeRepository studentAffairsOfficeRepository;

	@Autowired
	private ExamPeriodMapper mapper;

	public Optional<ExamPeriod> findOne(Long id) {
		return repository.findById(id);
	}

	public void populateRegisteredCounts(ExamPeriodDto dto) {
		if (dto.getTerms() == null) {
			return;
		}
		for (ExamPeriodTermDto term : dto.getTerms()) {
			term.setRegisteredCount(examAttemptRepository.countByExamPeriodTermId(term.getId()));
		}
	}

	public boolean isVisibleToStaff(ExamPeriod examPeriod, Long userId, boolean isAdmin, boolean isStudentAffairs) {
		return canManageCourse(examPeriod.getCourse(), userId, isAdmin, isStudentAffairs);
	}

	public Page<ExamPeriod> findVisibleToStaff(Long userId, boolean isAdmin, boolean isStudentAffairs, Pageable pageable) {
		if (isAdmin) {
			return repository.findAll(pageable);
		}
		StudentAffairsOffice office = studentAffairsOfficeRepository.findById(userId).orElse(null);
		if (office == null || office.getFaculty() == null) {
			return Page.empty(pageable);
		}
		return repository.findByCourse_StudyProgram_Faculty_Id(office.getFaculty().getId(), pageable);
	}

	public Page<ExamPeriod> findVisibleByCourse(Long courseId, Long userId, boolean isAdmin, boolean isStudentAffairs,
			boolean isTeacher, boolean isStudent, Pageable pageable) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found."));

		if (isAdmin || (isStudentAffairs && canManageCourse(course, userId, false, true))) {
			return repository.findByCourseId(courseId, pageable);
		}
		if (isTeacher && course.getTeacher().getId().equals(userId)) {
			return repository.findByCourseId(courseId, pageable);
		}
		if (isStudent && course.getStudents().stream().anyMatch(s -> s.getId().equals(userId))) {
			return repository.findOpenByCourseId(courseId, LocalDate.now(), pageable);
		}
		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to view exam periods for this course.");
	}

	public ExamPeriod create(ExamPeriodCreateDto dto, RegisteredUser createdBy, boolean isAdmin, boolean isStudentAffairs) {
		Course course = courseRepository.findById(dto.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException("Course not found: " + dto.getCourseId()));
		if (!canManageCourse(course, createdBy.getId(), isAdmin, isStudentAffairs)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"You are not allowed to create an exam period for this course.");
		}
		ExamPeriod examPeriod = mapper.toEntity(dto);
		examPeriod.setCourse(course);
		examPeriod.setCreatedBy(createdBy);
		return repository.save(examPeriod);
	}

	public ExamPeriod update(Long id, ExamPeriodUpdateDto dto, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		ExamPeriod examPeriod = repository.findById(id).orElse(null);
		if (examPeriod == null || !canManageCourse(examPeriod.getCourse(), requestingUserId, isAdmin, isStudentAffairs)) {
			return null;
		}
		mapper.updateEntityFromDto(dto, examPeriod);
		return repository.save(examPeriod);
	}

	public boolean delete(Long id, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		ExamPeriod examPeriod = repository.findById(id).orElse(null);
		if (examPeriod == null || !canManageCourse(examPeriod.getCourse(), requestingUserId, isAdmin, isStudentAffairs)) {
			return false;
		}
		if (examAttemptRepository.existsByExamPeriodId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete exam period: students have already registered for one of its terms.");
		}
		repository.deleteById(id);
		return true;
	}

	boolean canManageCourse(Course course, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		if (isAdmin) {
			return true;
		}
		if (isStudentAffairs) {
			StudentAffairsOffice office = studentAffairsOfficeRepository.findById(requestingUserId).orElse(null);
			Faculty courseFaculty = course.getStudyProgram() != null ? course.getStudyProgram().getFaculty() : null;
			return office != null && office.getFaculty() != null && courseFaculty != null
					&& office.getFaculty().getId().equals(courseFaculty.getId());
		}
		return false;
	}
}
