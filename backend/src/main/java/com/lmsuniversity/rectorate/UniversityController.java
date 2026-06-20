package com.lmsuniversity.rectorate;

import java.util.HashSet;
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

@Controller
@RequestMapping(path = "/api/universities")
@CrossOrigin(origins = "http://localhost:4200")
public class UniversityController {

	@Autowired
	private UniversityService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<UniversityDto>> getAll(){
		HashSet<UniversityDto> universities = new HashSet<UniversityDto>();
		for (University u : service.findAll()) {
			RectorateDto rectorate = new RectorateDto(u.getRectorate().getId(), u.getRectorate().getName(),
					u.getRectorate().getContact(), u.getRectorate().getImage(), u.getRectorate().getAddress(),
					u.getRectorate().getRectorName());
			universities.add(new UniversityDto(u.getId(), u.getName(), u.getFoundingDate(), u.getDescription() , u.getContact(),  u.getImage(), u.getAddress(), rectorate));
		}
		return new ResponseEntity<Iterable<UniversityDto>>(universities, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UniversityDto> get(@PathVariable("id") Long id){
		Optional<University> u = service.findOne(id);
		if(u.isPresent()) {
			RectorateDto rectorate = new RectorateDto(u.get().getRectorate().getId(), u.get().getRectorate().getName(),
					u.get().getRectorate().getContact(), u.get().getRectorate().getImage(), u.get().getRectorate().getAddress(),
					u.get().getRectorate().getRectorName());
			UniversityDto dto = new UniversityDto(u.get().getId(), u.get().getName(), u.get().getFoundingDate(), u.get().getContact(), u.get().getDescription(),  u.get().getImage(), u.get().getAddress(), rectorate);
			return new ResponseEntity<UniversityDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<UniversityDto>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<University> create(@RequestBody University r){
		try {
			service.save(r);
			return new ResponseEntity<University>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<University>(HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<University> update(@PathVariable("id") Long id, @RequestBody University university){
		University u = service.findOne(id).orElse(null);
		if(u != null) {
			university.setId(id);
			university.setRectorate(u.getRectorate());
			university = service.save(university);
			return new ResponseEntity<University>(university, HttpStatus.OK);
		}
		return new ResponseEntity<University>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<University> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<University>(HttpStatus.OK);
		}
		return new ResponseEntity<University>(HttpStatus.NOT_FOUND);
	}
}
