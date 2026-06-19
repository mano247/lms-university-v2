package rs.ac.singidunum.novisad.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.ExamAttempt;
import rs.ac.singidunum.novisad.backend.repository.ExamAttemptRepository;



@Service
public class ExamAttemptService {
	@Autowired
	private ExamAttemptRepository repository;

	public Iterable<ExamAttempt> findAll() {
		return repository.findAll();
	}

	public Optional<ExamAttempt> findOne(Long id) {
		return repository.findById(id);
	}


	public ExamAttempt save(ExamAttempt newExamAttempt) {
		return repository.save(newExamAttempt);
	}

	public ExamAttempt update(ExamAttempt examAttempt) {
		if(repository.findById(examAttempt.getId()).isPresent()) {
			return repository.save(examAttempt);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(ExamAttempt examAttempt) {
		repository.delete(examAttempt);
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
