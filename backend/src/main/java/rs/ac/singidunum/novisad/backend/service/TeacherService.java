package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.user.Teacher;
import rs.ac.singidunum.novisad.backend.repository.NastavnikRepository;



@Service
public class NastavnikService {
	@Autowired
	private NastavnikRepository repository;
	
	public Iterable<Teacher> findAll() {
		return repository.findAll();
	}
	
	public Optional<Teacher> findOne(Long id) {
		return repository.findById(id);
	}

	
	public Teacher save(Teacher novaProfesor) {
		return repository.save(novaProfesor);
	}
	
	public Teacher update(Teacher profesor) {
		if(repository.findById(profesor.getId()).isPresent()) {
			return repository.save(profesor);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(Teacher profesor) {
		repository.delete(profesor);
	}
}
