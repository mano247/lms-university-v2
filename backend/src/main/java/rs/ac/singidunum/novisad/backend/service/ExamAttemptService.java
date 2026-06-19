package rs.ac.singidunum.novisad.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.ExamAttempt;
import rs.ac.singidunum.novisad.backend.repository.PolaganjeRepository;



@Service
public class PolaganjeService {
	@Autowired
	private PolaganjeRepository repository;
	
	public Iterable<ExamAttempt> findAll() {
		return repository.findAll();
	}
	
	public Optional<ExamAttempt> findOne(Long id) {
		return repository.findById(id);
	}

	
	public ExamAttempt save(ExamAttempt novaPokusajPolaganja) {
		return repository.save(novaPokusajPolaganja);
	}
	
	public ExamAttempt update(ExamAttempt pokusajPolaganja) {
		if(repository.findById(pokusajPolaganja.getId()).isPresent()) {
			return repository.save(pokusajPolaganja);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(ExamAttempt pokusajPolaganja) {
		repository.delete(pokusajPolaganja);
	}

	public Iterable<ExamAttempt> findAllByStudent(Long id ) {
		List<ExamAttempt> examAttempts = new ArrayList<>();
		for(ExamAttempt pp : findAll()){
			if(pp.getStudent().getId() == id) {
				examAttempts.add(pp);
			}
		}
		return examAttempts;
	}
}

