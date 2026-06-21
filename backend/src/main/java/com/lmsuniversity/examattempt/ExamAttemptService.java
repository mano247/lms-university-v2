package com.lmsuniversity.examattempt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

	public List<ExamAttempt> findAll() {
		return repository.findAll();
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

	public Iterable<ExamAttempt> findAllByStudent(Long id ) {
		List<ExamAttempt> examAttempts = new ArrayList<>();
		for(ExamAttempt pp : findAll()){
			if(pp.getStudent().getId().equals(id)) {
				examAttempts.add(pp);
			}
		}
		return examAttempts;
	}
}
