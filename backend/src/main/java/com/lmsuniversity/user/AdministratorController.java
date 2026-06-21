package com.lmsuniversity.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(path = "/api/administrators")
public class AdministratorController {
	@Autowired
	private AdministratorService service;

	@Autowired
	private AdministratorMapper mapper;

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Page<AdministratorDto>> getAll(Pageable pageable){
		Page<AdministratorDto> administrators = service.findAll(pageable).map(mapper::toDto);
		return new ResponseEntity<Page<AdministratorDto>>(administrators, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<AdministratorDto> get(@PathVariable("id") Long id){
		Optional<Administrator> a = service.findOne(id);
		if(a.isPresent()) {
			return new ResponseEntity<AdministratorDto>(mapper.toDto(a.get()), HttpStatus.OK);
		}
		return new ResponseEntity<AdministratorDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<AdministratorDto> create(@Valid @RequestBody AdministratorCreateDto dto){
		Administrator administrator = service.create(dto);
		return new ResponseEntity<AdministratorDto>(mapper.toDto(administrator), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<AdministratorDto> update(@PathVariable("id") Long id, @Valid @RequestBody AdministratorUpdateDto dto, Authentication authentication){
		if (authentication.isAuthenticated()) {
			Administrator administrator = service.update(id, dto);
			if(administrator != null) {
				return new ResponseEntity<AdministratorDto>(mapper.toDto(administrator), HttpStatus.OK);
			}
		}
		return new ResponseEntity<AdministratorDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Administrator> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<Administrator>(HttpStatus.OK);
		}
		return new ResponseEntity<Administrator>(HttpStatus.NOT_FOUND);
	}


}
