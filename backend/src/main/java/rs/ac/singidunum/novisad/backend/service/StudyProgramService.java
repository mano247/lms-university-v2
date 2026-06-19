package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.academic.StudyProgram;
import rs.ac.singidunum.novisad.backend.repository.StudyProgramRepository;

@Service
public class StudyProgramService {
	@Autowired
	private StudyProgramRepository repository;

	public Iterable<StudyProgram> findAll() {
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

	public StudyProgram update(StudyProgram studyProgram) {
		if(repository.findById(studyProgram.getId()).isPresent()) {
			return repository.save(studyProgram);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(StudyProgram studyProgram) {
		repository.delete(studyProgram);
	}

}
