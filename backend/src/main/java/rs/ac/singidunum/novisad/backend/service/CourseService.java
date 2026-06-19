package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.academic.Course;
import rs.ac.singidunum.novisad.backend.repository.PredmetRepository;



@Service
public class PredmetService {
	@Autowired
	private PredmetRepository repository;

	public Iterable<Course> findAll() {
		return repository.findAll();
	}
	
	public Optional<Course> findOne(Long id) {
		return repository.findById(id);
	}
	
	public Optional<Course> findBysifraPredmeta(String SifraPredmet) {
		return repository.findBysifraPredmeta(SifraPredmet);
	}

	public Course save(Course noviPredmet) {
		return repository.save(noviPredmet);
	}
	
	public Course update(Course course) {
		if(repository.findById(course.getId()).isPresent()) {
			return repository.save(course);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(Course course) {
		repository.delete(course);
	}
}