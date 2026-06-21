package com.lmsuniversity.announcement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "/api/announcements")
public class AnnouncementController {

	@Autowired
	private AnnouncementService service;

	@Autowired
	private AnnouncementMapper mapper;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<AnnouncementDto>> getAll(){
		List<AnnouncementDto> announcements = new ArrayList<AnnouncementDto>();
		for (Announcement o : service.findAll()) {
			if(!(o instanceof CourseAnnouncement)) {
				announcements.add(mapper.toDto(o));
			}
		}
		return new ResponseEntity<List<AnnouncementDto>>(announcements, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<AnnouncementDto> get(@PathVariable("id") Long id){
		Optional<Announcement> o = service.findOne(id);
		if(o.isPresent()) {
			return new ResponseEntity<AnnouncementDto>(mapper.toDto(o.get()), HttpStatus.OK);
		}
		return new ResponseEntity<AnnouncementDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<AnnouncementDto> create(@Valid @RequestBody AnnouncementCreateDto dto){
		Announcement announcement = service.create(dto);
		return new ResponseEntity<AnnouncementDto>(mapper.toDto(announcement), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<AnnouncementDto> update(@PathVariable("id") Long id, @Valid @RequestBody AnnouncementUpdateDto dto){
		Announcement announcement = service.update(id, dto);
		if(announcement != null) {
			return new ResponseEntity<AnnouncementDto>(mapper.toDto(announcement), HttpStatus.OK);
		}
		return new ResponseEntity<AnnouncementDto>(HttpStatus.NOT_FOUND);
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
