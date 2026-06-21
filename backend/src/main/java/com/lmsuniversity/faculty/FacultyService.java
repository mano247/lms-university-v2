package com.lmsuniversity.faculty;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsuniversity.rectorate.University;
import com.lmsuniversity.rectorate.UniversityRepository;
import com.lmsuniversity.studyprogram.StudyProgram;

@Service
public class FacultyService {
	@Autowired
	private FacultyRepository repository;

	@Autowired
	private UniversityRepository universityRepository;

	@Autowired
	private FacultyMapper mapper;

	public List<Faculty> findAll() {
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

	public Faculty create(FacultyCreateDto dto) {
		University university = universityRepository.findById(dto.getUniversityId())
				.orElseThrow(() -> new IllegalArgumentException("University not found: " + dto.getUniversityId()));
		Faculty faculty = mapper.toEntity(dto);
		faculty.setUniversity(university);
		return repository.save(faculty);
	}

	public Faculty update(Long id, FacultyUpdateDto dto) {
		Faculty faculty = repository.findById(id).orElse(null);
		if (faculty == null) {
			return null;
		}
		University university = universityRepository.findById(dto.getUniversityId())
				.orElseThrow(() -> new IllegalArgumentException("University not found: " + dto.getUniversityId()));
		mapper.updateEntityFromDto(dto, faculty);
		faculty.setUniversity(university);
		return repository.save(faculty);
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
