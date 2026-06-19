package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.FinalThesis;
import rs.ac.singidunum.novisad.backend.repository.FinalThesisRepository;

@Service
public class FinalThesisService {
	@Autowired
	private FinalThesisRepository repository;

	public Iterable<FinalThesis> findAll() {
		return repository.findAll();
	}

	public Optional<FinalThesis> findOne(Long id) {
		return repository.findById(id);
	}


	public FinalThesis save(FinalThesis newFinalThesis) {
		return repository.save(newFinalThesis);
	}

	public FinalThesis update(FinalThesis finalThesis) {
		if(repository.findById(finalThesis.getId()).isPresent()) {
			return repository.save(finalThesis);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(FinalThesis finalThesis) {
		repository.delete(finalThesis);
	}
}
