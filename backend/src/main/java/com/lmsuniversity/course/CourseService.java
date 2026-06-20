package com.lmsuniversity.course;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
	@Autowired
	private CourseRepository repository;

	public Iterable<Course> findAll() {
		return repository.findAll();
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

	public Course update(Course course) {
		if(repository.findById(course.getId()).isPresent()) {
			return repository.save(course);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(Course course) {
		repository.delete(course);
	}
}
