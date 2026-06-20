package com.lmsuniversity.rectorate;

import java.util.HashSet;
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
@RequestMapping(path = "/api/universities")
public class UniversityController {

	@Autowired
	private UniversityService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<UniversityDto>> getAll(){
		HashSet<UniversityDto> universities = new HashSet<UniversityDto>();
		for (University u : service.findAll()) {
			RectorateDto rectorate = RectorateDto.builder()
					.id(u.getRectorate().getId())
					.name(u.getRectorate().getName())
					.contact(u.getRectorate().getContact())
					.image(u.getRectorate().getImage())
					.address(u.getRectorate().getAddress())
					.rectorName(u.getRectorate().getRectorName())
					.build();
			universities.add(UniversityDto.builder()
					.id(u.getId())
					.name(u.getName())
					.foundingDate(u.getFoundingDate())
					.contact(u.getContact())
					.description(u.getDescription())
					.image(u.getImage())
					.address(u.getAddress())
					.rectorate(rectorate)
					.build());
		}
		return new ResponseEntity<Iterable<UniversityDto>>(universities, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UniversityDto> get(@PathVariable("id") Long id){
		Optional<University> u = service.findOne(id);
		if(u.isPresent()) {
			RectorateDto rectorate = RectorateDto.builder()
					.id(u.get().getRectorate().getId())
					.name(u.get().getRectorate().getName())
					.contact(u.get().getRectorate().getContact())
					.image(u.get().getRectorate().getImage())
					.address(u.get().getRectorate().getAddress())
					.rectorName(u.get().getRectorate().getRectorName())
					.build();
			UniversityDto dto = UniversityDto.builder()
					.id(u.get().getId())
					.name(u.get().getName())
					.foundingDate(u.get().getFoundingDate())
					.contact(u.get().getContact())
					.description(u.get().getDescription())
					.image(u.get().getImage())
					.address(u.get().getAddress())
					.rectorate(rectorate)
					.build();
			return new ResponseEntity<UniversityDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<UniversityDto>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<University> create(@Valid @RequestBody University r){
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
	public ResponseEntity<University> update(@PathVariable("id") Long id, @Valid @RequestBody University university){
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
