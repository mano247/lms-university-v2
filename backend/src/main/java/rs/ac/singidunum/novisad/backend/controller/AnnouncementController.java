package rs.ac.singidunum.novisad.backend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rs.ac.singidunum.novisad.backend.model.Announcement;
import rs.ac.singidunum.novisad.backend.model.CourseAnnouncement;
import rs.ac.singidunum.novisad.backend.service.AnnouncementService;

@Controller
@RequestMapping(path = "/api/announcements")
@CrossOrigin(origins = "http://localhost:4200")
public class AnnouncementController {

	@Autowired
	private AnnouncementService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Announcement>> getAll(){
		ArrayList<Announcement> announcements = new ArrayList<Announcement>();
		for (Announcement o : service.findAll()) {

			if(!(o instanceof CourseAnnouncement)) {
				announcements.add(new Announcement(o.getId(), o.getTitle(), o.getContent(), o.getDate(), o.getImage(), o.getStartDate(), o.getEndDate()));
			}
		}
		return new ResponseEntity<Iterable<Announcement>>(announcements, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Announcement> get(@PathVariable("id") Long id){
		Optional<Announcement> o = service.findOne(id);
		if(o.isPresent()) {
			
			Announcement announcement = new Announcement(o.get().getId(), o.get().getTitle(), o.get().getContent(), o.get().getDate(), o.get().getImage(), o.get().getStartDate(), o.get().getEndDate());
			return new ResponseEntity<Announcement>(announcement, HttpStatus.OK);
		}
		return new ResponseEntity<Announcement>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Announcement> create(@RequestBody Announcement r){
		try {
			r.setDate(LocalDateTime.now());
			service.save(r);
			return new ResponseEntity<Announcement>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Announcement>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Announcement> update(@PathVariable("id") Long id, @RequestBody Announcement announcement){
		Announcement r = service.findOne(id).orElse(null);
		if(r != null) {
			announcement.setId(id);
			if(announcement.getImage() == null || announcement.getImage() == "") {
				announcement.setImage(r.getImage());
			}
			announcement.setDate(LocalDateTime.now());
			announcement = service.save(announcement);
			return new ResponseEntity<Announcement>(announcement, HttpStatus.OK);
		}
		return new ResponseEntity<Announcement>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Announcement> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<Announcement>(HttpStatus.OK);
		}
		return new ResponseEntity<Announcement>(HttpStatus.NOT_FOUND);
	}
}
