package com.lmsuniversity.rectorate;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

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
@RequestMapping(path = "/api/rectorates")
public class RectorateController {
	@Autowired
	private RectorateService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<RectorateDto>> getAll(){
		HashSet<RectorateDto> rectorate = new HashSet<RectorateDto>();
		for (Rectorate r : service.findAll()) {
			Set<UniversityDto> universities = r.getUniversities().stream().
					map(university -> UniversityDto.builder()
							.id(university.getId())
							.name(university.getName())
							.foundingDate(university.getFoundingDate())
							.contact(university.getContact())
							.description(university.getDescription())
							.image(university.getImage())
							.address(university.getAddress())
							.build()).
					collect(Collectors.toSet());
			rectorate.add(RectorateDto.builder()
					.id(r.getId())
					.name(r.getName())
					.contact(r.getContact())
					.image(r.getImage())
					.address(r.getAddress())
					.rectorName(r.getRectorName())
					.universities(universities)
					.build());
		}
		return new ResponseEntity<Iterable<RectorateDto>>(rectorate, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<RectorateDto> get(@PathVariable("id") Long id){
		Optional<Rectorate> r = service.findOne(id);
		if(r.isPresent()) {
			Set<UniversityDto> universities = r.get().getUniversities().stream().
					map(university -> UniversityDto.builder()
							.id(university.getId())
							.name(university.getName())
							.foundingDate(university.getFoundingDate())
							.contact(university.getContact())
							.description(university.getDescription())
							.image(university.getImage())
							.address(university.getAddress())
							.build()).
					collect(Collectors.toSet());
			RectorateDto dto = RectorateDto.builder()
					.id(r.get().getId())
					.name(r.get().getName())
					.contact(r.get().getContact())
					.image(r.get().getImage())
					.address(r.get().getAddress())
					.rectorName(r.get().getRectorName())
					.universities(universities)
					.build();
			return new ResponseEntity<RectorateDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<RectorateDto>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Rectorate> create(@Valid @RequestBody Rectorate r){
		try {
			service.save(r);
			return new ResponseEntity<Rectorate>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Rectorate>(HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Rectorate> update(@PathVariable("id") Long id, @Valid @RequestBody Rectorate rectorate){
		Rectorate r = service.findOne(id).orElse(null);
		if(r != null) {
			rectorate.setId(id);
			rectorate = service.save(rectorate);
			return new ResponseEntity<Rectorate>(rectorate, HttpStatus.OK);
		}
		return new ResponseEntity<Rectorate>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Rectorate> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<Rectorate>(HttpStatus.OK);
		}
		return new ResponseEntity<Rectorate>(HttpStatus.NOT_FOUND);
	}
}
