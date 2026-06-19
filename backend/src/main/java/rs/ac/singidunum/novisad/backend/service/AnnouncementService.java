package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.Announcement;
import rs.ac.singidunum.novisad.backend.repository.ObavestenjeRepository;



@Service
public class ObavestenjaService {
	@Autowired
	private ObavestenjeRepository repository;
	
	public Iterable<Announcement> findAll() {
		return repository.findAll();
	}
	
	public Optional<Announcement> findOne(Long id) {
		return repository.findById(id);
	}

	
	public Announcement save(Announcement novaObavestenja) {
		return repository.save(novaObavestenja);
	}
	
	public Announcement update(Announcement announcements) {
		if(repository.findById(announcements.getId()).isPresent()) {
			return repository.save(announcements);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(Announcement announcements) {
		repository.delete(announcements);
	}
}
