package com.lmsuniversity.course;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lmsuniversity.examattempt.ExamAttemptRepository;
import com.lmsuniversity.examperiod.ExamPeriodRepository;
import com.lmsuniversity.studyprogram.StudyProgram;
import com.lmsuniversity.studyprogram.StudyProgramRepository;
import com.lmsuniversity.studyyear.StudyYear;
import com.lmsuniversity.studyyear.StudyYearRepository;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.user.StudentRepository;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.TeacherRepository;

@Service
public class CourseService {
	@Autowired
	private CourseRepository repository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private StudyProgramRepository studyProgramRepository;

	@Autowired
	private StudyYearRepository studyYearRepository;

	@Autowired
	private ExamAttemptRepository examAttemptRepository;

	@Autowired
	private ExamPeriodRepository examPeriodRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseMapper mapper;

	public Page<Course> findAll(Pageable pageable) {
		Page<Course> page = repository.findAll(pageable);
		List<Course> withDetails = repository.fetchDetails(page.getContent());
		Map<Long, Course> byId = new LinkedHashMap<>();
		withDetails.forEach(c -> byId.put(c.getId(), c));
		List<Course> ordered = page.getContent().stream().map(c -> byId.get(c.getId())).toList();
		return new PageImpl<>(ordered, pageable, page.getTotalElements());
	}

	public Optional<Course> findOne(Long id) {
		return repository.findById(id);
	}

	public Optional<Course> findByCourseCode(String courseCode) {
		return repository.findByCourseCode(courseCode);
	}

	public Course save(Course newCourse) {
		return repository.save(newCourse);
	}

	public Course create(CourseCreateDto dto) {
		Teacher teacher = teacherRepository.findById(dto.getTeacherId())
				.orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + dto.getTeacherId()));
		StudyProgram studyProgram = studyProgramRepository.findById(dto.getStudyProgramId())
				.orElseThrow(() -> new IllegalArgumentException("Study program not found: " + dto.getStudyProgramId()));
		StudyYear studyYear = studyYearRepository.findById(dto.getStudyYearId())
				.orElseThrow(() -> new IllegalArgumentException("Study year not found: " + dto.getStudyYearId()));
		Course course = mapper.toEntity(dto);
		course.setTeacher(teacher);
		course.setStudyProgram(studyProgram);
		course.setStudyYear(studyYear);
		return repository.save(course);
	}

	public Course update(Long id, CourseUpdateDto dto) {
		Course course = repository.findById(id).orElse(null);
		if (course == null) {
			return null;
		}
		Teacher teacher = teacherRepository.findById(dto.getTeacherId())
				.orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + dto.getTeacherId()));
		StudyProgram studyProgram = studyProgramRepository.findById(dto.getStudyProgramId())
				.orElseThrow(() -> new IllegalArgumentException("Study program not found: " + dto.getStudyProgramId()));
		StudyYear studyYear = studyYearRepository.findById(dto.getStudyYearId())
				.orElseThrow(() -> new IllegalArgumentException("Study year not found: " + dto.getStudyYearId()));
		mapper.updateEntityFromDto(dto, course);
		course.setTeacher(teacher);
		course.setStudyProgram(studyProgram);
		course.setStudyYear(studyYear);
		return repository.save(course);
	}

	public Course assignTeacher(Long id, Long teacherId) {
		Course course = repository.findById(id).orElse(null);
		if (course == null) {
			return null;
		}
		Teacher teacher = teacherRepository.findById(teacherId)
				.orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + teacherId));
		course.setTeacher(teacher);
		return repository.save(course);
	}

	public Course updateSyllabus(Long id, String syllabus, Long requestingTeacherId) {
		Course course = repository.findById(id).orElse(null);
		if (course == null || !course.getTeacher().getId().equals(requestingTeacherId)) {
			return null;
		}
		course.setSyllabus(syllabus);
		return repository.save(course);
	}

	public void delete(Long id) {
		if (examAttemptRepository.existsByCourseId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete course: it still has exam attempts associated with it.");
		}
		if (examPeriodRepository.existsByCourseId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete course: it still has exam periods associated with it.");
		}
		Course course = repository.findById(id).orElse(null);
		if (course != null && course.getStudents() != null) {
			for (Student student : new HashSet<>(course.getStudents())) {
				student.getCourses().remove(course);
				studentRepository.save(student);
			}
		}
		repository.deleteById(id);
	}

	public void delete(Course course) {
		repository.delete(course);
	}
}
