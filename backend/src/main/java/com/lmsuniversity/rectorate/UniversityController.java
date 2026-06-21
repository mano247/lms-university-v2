package com.lmsuniversity.rectorate;

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
@RequestMapping(path = "/api/universities")
public class UniversityController {

	@Autowired
	private UniversityService service;

	@Autowired
	private UniversityMapper mapper;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<UniversityDto>> getAll(){
		List<UniversityDto> universities = mapper.toDtoList(service.findAll());
		return new ResponseEntity<List<UniversityDto>>(universities, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UniversityDto> get(@PathVariable("id") Long id){
		Optional<University> u = service.findOne(id);
		if(u.isPresent()) {
			return new ResponseEntity<UniversityDto>(mapper.toDto(u.get()), HttpStatus.OK);
		}
		return new ResponseEntity<UniversityDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<UniversityDto> create(@Valid @RequestBody UniversityCreateDto dto){
		University university = service.create(dto);
		return new ResponseEntity<UniversityDto>(mapper.toDto(university), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UniversityDto> update(@PathVariable("id") Long id, @Valid @RequestBody UniversityUpdateDto dto){
		University university = service.update(id, dto);
		if(university != null) {
			return new ResponseEntity<UniversityDto>(mapper.toDto(university), HttpStatus.OK);
		}
		return new ResponseEntity<UniversityDto>(HttpStatus.NOT_FOUND);
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
