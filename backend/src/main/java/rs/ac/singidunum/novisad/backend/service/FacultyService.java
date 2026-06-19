package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.academic.Faculty;
import rs.ac.singidunum.novisad.backend.model.academic.StudyProgram;
import rs.ac.singidunum.novisad.backend.repository.FakultetRepository;

@Service
public class FakultetService {
	@Autowired
	private FakultetRepository repository;
	
	public Iterable<Faculty> findAll() {
		return repository.findAll();
	}
	
	public Optional<Faculty> findOne(Long id) {
		return repository.findById(id);
	}
	
	public Optional<Faculty> findBySifra(String facultyCode) {
		return repository.findBySifraFakulteta(facultyCode);
	}
	
	public Faculty save(Faculty novaFakultet) {
		return repository.save(novaFakultet);
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
