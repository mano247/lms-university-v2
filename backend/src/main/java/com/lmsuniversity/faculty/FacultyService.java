package com.lmsuniversity.faculty;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsuniversity.studyprogram.StudyProgram;

@Service
public class FacultyService {
	@Autowired
	private FacultyRepository repository;

	public Iterable<Faculty> findAll() {
		return repository.findAll();
	}

	public Optional<Faculty> findOne(Long id) {
		return repository.findById(id);
	}

	public Optional<Faculty> findByCode(String facultyCode) {
		return repository.findByFacultyCode(facultyCode);
	}

	public Faculty save(Faculty newFaculty) {
		return repository.save(newFaculty);
	}

	public Faculty update(Faculty faculty) {
		if(repository.findById(faculty.getId()).isPresent()) {
			return repository.save(faculty);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(Faculty faculty) {
		repository.delete(faculty);
	}

	@SuppressWarnings("deprecation")
	public Set<StudyProgram> getAllCoursesOfFaculty(Long id) {
		return repository.getById(id).getStudyPrograms();
	}
}
