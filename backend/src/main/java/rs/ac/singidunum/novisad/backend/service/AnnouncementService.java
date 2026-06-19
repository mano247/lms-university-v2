package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.Announcement;
import rs.ac.singidunum.novisad.backend.repository.AnnouncementRepository;



@Service
public class AnnouncementService {
	@Autowired
	private AnnouncementRepository repository;

	public Iterable<Announcement> findAll() {
		return repository.findAll();
	}

	public Optional<Announcement> findOne(Long id) {
		return repository.findById(id);
	}


	public Announcement save(Announcement newAnnouncement) {
		return repository.save(newAnnouncement);
	}

	public Announcement update(Announcement announcement) {
		if(repository.findById(announcement.getId()).isPresent()) {
			return repository.save(announcement);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(Announcement announcement) {
		repository.delete(announcement);
	}
}
