package com.lmsuniversity.studyprogram;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.faculty.FacultyRepository;

@Service
public class StudyProgramService {
	@Autowired
	private StudyProgramRepository repository;

	@Autowired
	private FacultyRepository facultyRepository;

	@Autowired
	private StudyProgramMapper mapper;

	public List<StudyProgram> findAll() {
		return repository.findAll();
	}

	public Optional<StudyProgram> findOne(Long id) {
		return repository.findById(id);
	}

	public Optional<StudyProgram> findByProgramCode(String programCode) {
		return repository.findByProgramCode(programCode);
	}

	public StudyProgram save(StudyProgram newStudyProgram) {
		return repository.save(newStudyProgram);
	}

	public StudyProgram create(StudyProgramCreateDto dto) {
		Faculty faculty = facultyRepository.findById(dto.getFacultyId())
				.orElseThrow(() -> new IllegalArgumentException("Faculty not found: " + dto.getFacultyId()));
		StudyProgram studyProgram = mapper.toEntity(dto);
		studyProgram.setFaculty(faculty);
		return repository.save(studyProgram);
	}

	public StudyProgram update(Long id, StudyProgramUpdateDto dto) {
		StudyProgram studyProgram = repository.findById(id).orElse(null);
		if (studyProgram == null) {
			return null;
		}
		Faculty faculty = facultyRepository.findById(dto.getFacultyId())
				.orElseThrow(() -> new IllegalArgumentException("Faculty not found: " + dto.getFacultyId()));
		mapper.updateEntityFromDto(dto, studyProgram);
		studyProgram.setFaculty(faculty);
		return repository.save(studyProgram);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(StudyProgram studyProgram) {
		repository.delete(studyProgram);
	}

}
