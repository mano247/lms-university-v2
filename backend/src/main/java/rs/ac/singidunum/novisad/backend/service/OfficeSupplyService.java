package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.OfficeSupply;
import rs.ac.singidunum.novisad.backend.repository.KancelariskiMaterijalRepository;

@Service
public class KancelariskiMaterialService {
	@Autowired
	private KancelariskiMaterijalRepository repository;
	
	public Iterable<OfficeSupply> findAll() {
		return repository.findAll();
	}
	
	public Optional<OfficeSupply> findOne(Long id) {
		return repository.findById(id);
	}

	
	public OfficeSupply save(OfficeSupply nova) {
		return repository.save(nova);
	}
	
	public OfficeSupply update(OfficeSupply kancelariskiMaterijal) {
		if(repository.findById(kancelariskiMaterijal.getId()).isPresent()) {
			return repository.save(kancelariskiMaterijal);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(OfficeSupply kancelariskiMaterijal) {
		repository.delete(kancelariskiMaterijal);
	}
}
