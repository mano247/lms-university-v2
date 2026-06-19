package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.user.StudentAffairsOffice;
import rs.ac.singidunum.novisad.backend.repository.StudentskaSluzbaRepository;



@Service
public class StudentskaSluzbaService {
	@Autowired
	private StudentskaSluzbaRepository repository;
	
	public Iterable<StudentAffairsOffice> findAll() {
		return repository.findAll();
	}
	
	public Optional<StudentAffairsOffice> findOne(Long id) {
		return repository.findById(id);
	}

	
	public StudentAffairsOffice save(StudentAffairsOffice novaStudentskaSluzba) {
		return repository.save(novaStudentskaSluzba);
	}
	
	public StudentAffairsOffice update(StudentAffairsOffice studentskaSluzba) {
		if(repository.findById(studentskaSluzba.getId()).isPresent()) {
			return repository.save(studentskaSluzba);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(StudentAffairsOffice studentskaSluzba) {
		repository.delete(studentskaSluzba);
	}
}
