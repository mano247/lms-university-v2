package com.lmsuniversity.faculty;

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
@RequestMapping(path = "/api/faculties")
public class FacultyController {
	@Autowired
	private FacultyService service;

	@Autowired
	private FacultyMapper mapper;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<FacultyDto>> getAll(){
		List<FacultyDto> faculties = mapper.toDtoList(service.findAll());
		return new ResponseEntity<List<FacultyDto>>(faculties, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FacultyDto> get(@PathVariable("id") Long id){
		Optional<Faculty> f = service.findOne(id);
		if(f.isPresent()) {
			return new ResponseEntity<FacultyDto>(mapper.toDto(f.get()), HttpStatus.OK);
		}
		return new ResponseEntity<FacultyDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/code/{facultyCode}", method = RequestMethod.GET)
	public ResponseEntity<FacultyDto> get(@PathVariable("facultyCode") String facultyCode){
		Optional<Faculty> f = service.findByCode(facultyCode);
		if(f.isPresent()) {
			return new ResponseEntity<FacultyDto>(mapper.toDto(f.get()), HttpStatus.OK);
		}
		return new ResponseEntity<FacultyDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<FacultyDto> create(@Valid @RequestBody FacultyCreateDto dto){
		Faculty faculty = service.create(dto);
		return new ResponseEntity<FacultyDto>(mapper.toDto(faculty), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<FacultyDto> update(@PathVariable("id") Long id, @Valid @RequestBody FacultyUpdateDto dto){
		Faculty faculty = service.update(id, dto);
		if(faculty != null) {
			return new ResponseEntity<FacultyDto>(mapper.toDto(faculty), HttpStatus.OK);
		}
		return new ResponseEntity<FacultyDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Faculty> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<Faculty>(HttpStatus.OK);
		}
		return new ResponseEntity<Faculty>(HttpStatus.NOT_FOUND);
	}
}
