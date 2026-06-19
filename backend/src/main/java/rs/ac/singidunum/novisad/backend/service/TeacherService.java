package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.user.Teacher;
import rs.ac.singidunum.novisad.backend.repository.TeacherRepository;



@Service
public class TeacherService {
	@Autowired
	private TeacherRepository repository;

	public Iterable<Teacher> findAll() {
		return repository.findAll();
	}

	public Optional<Teacher> findOne(Long id) {
		return repository.findById(id);
	}


	public Teacher save(Teacher newTeacher) {
		return repository.save(newTeacher);
	}

	public Teacher update(Teacher teacher) {
		if(repository.findById(teacher.getId()).isPresent()) {
			return repository.save(teacher);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(Teacher teacher) {
		repository.delete(teacher);
	}
}
