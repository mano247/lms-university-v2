package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.user.Student;
import rs.ac.singidunum.novisad.backend.repository.StudentRepository;

@Service
public class StudentService {
	@Autowired
	private StudentRepository repository;
	
	public Iterable<Student> findAll() {
		return repository.findAll();
	}
	
	public Optional<Student> findOne(Long id) {
		return repository.findById(id);
	}

	
	public Student save(Student newStudent) {
		return repository.save(newStudent);
	}
	
	public Student update(Student student) {
		if(repository.findById(student.getId()).isPresent()) {
			return repository.save(student);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(Student student) {
		repository.delete(student);
	}
}
