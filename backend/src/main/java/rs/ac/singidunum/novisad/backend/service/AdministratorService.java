package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.user.Administrator;
import rs.ac.singidunum.novisad.backend.repository.AdministratorRepository;



@Service
public class AdministratorService {
	@Autowired
	private AdministratorRepository repository;
	
	public Iterable<Administrator> findAll() {
		return repository.findAll();
	}
	
	public Optional<Administrator> findOne(Long id) {
		return repository.findById(id);
	}

	
	public Administrator save(Administrator newAdministrator) {
		return repository.save(newAdministrator);
	}
	
	public Administrator update(Administrator administrator) {
		if(repository.findById(administrator.getId()).isPresent()) {
			return repository.save(administrator);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(Administrator administrator) {
		repository.delete(administrator);
	}
}
