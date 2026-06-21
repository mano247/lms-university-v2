package com.lmsuniversity.course;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lmsuniversity.studyprogram.StudyProgram;
import com.lmsuniversity.studyprogram.StudyProgramRepository;
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
		Course course = mapper.toEntity(dto);
		course.setTeacher(teacher);
		course.setStudyProgram(studyProgram);
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
		mapper.updateEntityFromDto(dto, course);
		course.setTeacher(teacher);
		course.setStudyProgram(studyProgram);
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
		repository.deleteById(id);
	}

	public void delete(Course course) {
		repository.delete(course);
	}
}
