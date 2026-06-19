package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.OfficeSupply;
import rs.ac.singidunum.novisad.backend.repository.OfficeSupplyRepository;

@Service
public class OfficeSupplyService {
	@Autowired
	private OfficeSupplyRepository repository;

	public Iterable<OfficeSupply> findAll() {
		return repository.findAll();
	}

	public Optional<OfficeSupply> findOne(Long id) {
		return repository.findById(id);
	}


	public OfficeSupply save(OfficeSupply newOfficeSupply) {
		return repository.save(newOfficeSupply);
	}

	public OfficeSupply update(OfficeSupply officeSupply) {
		if(repository.findById(officeSupply.getId()).isPresent()) {
			return repository.save(officeSupply);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(OfficeSupply officeSupply) {
		repository.delete(officeSupply);
	}
}
