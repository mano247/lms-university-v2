package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.academic.StudyProgram;
import rs.ac.singidunum.novisad.backend.repository.StudijskiProgramRepository;

@Service
public class StudijskiProgramService {
	@Autowired
	private StudijskiProgramRepository repository;
	
	public Iterable<StudyProgram> findAll() {
		return repository.findAll();
	}
	
	public Optional<StudyProgram> findOne(Long id) {
		return repository.findById(id);
	}
	
	public Optional<StudyProgram> findBySifraSP(String programCode) {
		return repository.findBySifraSP(programCode);
	}
	
	public StudyProgram save(StudyProgram noviStudijskiProgram) {
		return repository.save(noviStudijskiProgram);
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
