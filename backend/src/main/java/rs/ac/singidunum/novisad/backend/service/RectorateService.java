package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.Rectorate;
import rs.ac.singidunum.novisad.backend.model.academic.University;
import rs.ac.singidunum.novisad.backend.repository.RectorateRepository;

@Service
public class RectorateService {

	@Autowired
	private RectorateRepository repository;


	public Iterable<Rectorate> findAll() {
		return repository.findAll();
	}

	public Optional<Rectorate> findOne(Long id) {
		return repository.findById(id);
	}


	public Rectorate save(Rectorate newRectorate) {
		return repository.save(newRectorate);
	}

	public Rectorate update(Rectorate rectorate) {
		if(repository.findById(rectorate.getId()).isPresent()) {
			return repository.save(rectorate);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(Rectorate rectorate) {
		repository.delete(rectorate);
	}

	@SuppressWarnings("deprecation")
	public Set<University> getAllUniversitiesForRectorate(Long id) {
		return repository.getById(id).getUniversities();
	}
}
