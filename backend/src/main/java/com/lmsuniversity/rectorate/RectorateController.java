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
@RequestMapping(path = "/api/rectorates")
public class RectorateController {
	@Autowired
	private RectorateService service;

	@Autowired
	private RectorateMapper mapper;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<RectorateDto>> getAll(){
		List<RectorateDto> rectorates = mapper.toDtoList(service.findAll());
		return new ResponseEntity<List<RectorateDto>>(rectorates, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<RectorateDto> get(@PathVariable("id") Long id){
		Optional<Rectorate> r = service.findOne(id);
		if(r.isPresent()) {
			return new ResponseEntity<RectorateDto>(mapper.toDto(r.get()), HttpStatus.OK);
		}
		return new ResponseEntity<RectorateDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<RectorateDto> create(@Valid @RequestBody RectorateCreateDto dto){
		Rectorate rectorate = service.create(dto);
		return new ResponseEntity<RectorateDto>(mapper.toDto(rectorate), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<RectorateDto> update(@PathVariable("id") Long id, @Valid @RequestBody RectorateUpdateDto dto){
		Rectorate rectorate = service.update(id, dto);
		if(rectorate != null) {
			return new ResponseEntity<RectorateDto>(mapper.toDto(rectorate), HttpStatus.OK);
		}
		return new ResponseEntity<RectorateDto>(HttpStatus.NOT_FOUND);
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
