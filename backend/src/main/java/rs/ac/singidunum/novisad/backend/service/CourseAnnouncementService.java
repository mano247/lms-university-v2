package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.CourseAnnouncement;
import rs.ac.singidunum.novisad.backend.repository.PredmetnaObavestenjaRepository;


@Service
public class PredmetnaObavestenjaService {
	@Autowired
	private PredmetnaObavestenjaRepository repository;
	
	public Iterable<CourseAnnouncement> findAll() {
		return repository.findAll();
	}
	
	public Optional<CourseAnnouncement> findOne(Long id) {
		return repository.findById(id);
	}

	
	public CourseAnnouncement save(CourseAnnouncement novaOpstaObavestenja) {
		return repository.save(novaOpstaObavestenja);
	}
	
	public CourseAnnouncement update(CourseAnnouncement opstaObavestenja) {
		if(repository.findById(opstaObavestenja.getId()).isPresent()) {
			return repository.save(opstaObavestenja);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(CourseAnnouncement opstaObavestenja) {
		repository.delete(opstaObavestenja);
	}
}
