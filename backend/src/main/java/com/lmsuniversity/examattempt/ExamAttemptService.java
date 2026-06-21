package com.lmsuniversity.examattempt;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lmsuniversity.course.Course;
import com.lmsuniversity.course.CourseRepository;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.user.StudentRepository;

@Service
public class ExamAttemptService {
	@Autowired
	private ExamAttemptRepository repository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ExamAttemptMapper mapper;

	public Page<ExamAttempt> findAll(Pageable pageable) {
		Page<ExamAttempt> page = repository.findAll(pageable);
		List<ExamAttempt> withDetails = repository.fetchDetails(page.getContent());
		Map<Long, ExamAttempt> byId = new LinkedHashMap<>();
		withDetails.forEach(e -> byId.put(e.getId(), e));
		List<ExamAttempt> ordered = page.getContent().stream().map(e -> byId.get(e.getId())).toList();
		return new PageImpl<>(ordered, pageable, page.getTotalElements());
	}

	public Optional<ExamAttempt> findOne(Long id) {
		return repository.findById(id);
	}


	public ExamAttempt save(ExamAttempt newExamAttempt) {
		return repository.save(newExamAttempt);
	}

	public ExamAttempt create(ExamAttemptCreateDto dto, Long studentId) {
		Course course = courseRepository.findById(dto.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException("Course not found: " + dto.getCourseId()));
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
		if (!course.getStudents().contains(student)) {
			throw new IllegalArgumentException("Student is not enrolled in course: " + dto.getCourseId());
		}

		ExamAttempt examAttempt = mapper.toEntity(dto);
		examAttempt.setCourse(course);
		examAttempt.setStudent(student);
		examAttempt.setTeacher(course.getTeacher());
		examAttempt.setPoints(0);
		examAttempt.setFinalGrade(0);
		return repository.save(examAttempt);
	}

	public ExamAttempt update(Long id, ExamAttemptUpdateDto dto, Long requestingTeacherId) {
		ExamAttempt examAttempt = repository.findById(id).orElse(null);
		if (examAttempt == null || !examAttempt.getTeacher().getId().equals(requestingTeacherId)) {
			return null;
		}
		if (dto.getPoints() != null) {
			examAttempt.setPoints(dto.getPoints());
		}
		if (dto.getFinalGrade() != null) {
			examAttempt.setFinalGrade(dto.getFinalGrade());
		}
		if (dto.getNote() != null) {
			examAttempt.setNote(dto.getNote());
		}
		return repository.save(examAttempt);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(ExamAttempt examAttempt) {
		repository.delete(examAttempt);
	}

	public List<ExamAttempt> findRegisteredByStudent(Long studentId) {
		return repository.findRegisteredByStudent(studentId);
	}

	public List<ExamAttempt> findRegisteredByCourse(Long courseId) {
		return repository.findRegisteredByCourse(courseId);
	}

	public List<ExamAttempt> findByCourseIds(List<Long> courseIds) {
		return repository.findByCourseIds(courseIds);
	}

	public List<ExamAttempt> findByStudentId(Long studentId) {
		return repository.findByStudentId(studentId);
	}
}
