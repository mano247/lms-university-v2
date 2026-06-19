package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.FinalThesis;
import rs.ac.singidunum.novisad.backend.repository.ZavrsniRadRepository;

@Service
public class ZavrsniRadService {
	@Autowired
	private ZavrsniRadRepository zrRepository;
	
	public Iterable<FinalThesis> findAll() {
		return zrRepository.findAll();
	}
	
	public Optional<FinalThesis> findOne(Long id) {
		return zrRepository.findById(id);
	}

	
	public FinalThesis save(FinalThesis noviZavrsniRad) {
		return zrRepository.save(noviZavrsniRad);
	}
	
	public FinalThesis update(FinalThesis FinalThesis) {
		if(zrRepository.findById(FinalThesis.getId()).isPresent()) {
			return zrRepository.save(FinalThesis);
		}
		return null;
	}
	
	public void delete(Long id) {
		zrRepository.deleteById(id);
	}
	
	public void delete(FinalThesis FinalThesis) {
		zrRepository.delete(FinalThesis);
	}
}
