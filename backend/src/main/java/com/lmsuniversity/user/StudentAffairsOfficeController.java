package com.lmsuniversity.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(path = "/api/student-affairs-office")
public class StudentAffairsOfficeController {
	@Autowired
	private StudentAffairsOfficeService service;

	@Autowired
	private StudentAffairsOfficeMapper mapper;

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<StudentAffairsOfficeDto>> getAll(){
		List<StudentAffairsOfficeDto> studentAffairsOffices = mapper.toDtoList(service.findAll());
		return new ResponseEntity<List<StudentAffairsOfficeDto>>(studentAffairsOffices, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<StudentAffairsOfficeDto> get(@PathVariable("id") Long id){
		Optional<StudentAffairsOffice> s = service.findOne(id);
		if(s.isPresent()) {
			return new ResponseEntity<StudentAffairsOfficeDto>(mapper.toDto(s.get()), HttpStatus.OK);
		}
		return new ResponseEntity<StudentAffairsOfficeDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<StudentAffairsOfficeDto> create(@Valid @RequestBody StudentAffairsOfficeCreateDto dto){
		StudentAffairsOffice studentAffairsOffice = service.create(dto);
		return new ResponseEntity<StudentAffairsOfficeDto>(mapper.toDto(studentAffairsOffice), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION','STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<StudentAffairsOfficeDto> update(@PathVariable("id") Long id, @Valid @RequestBody StudentAffairsOfficeUpdateDto dto, Authentication authentication){
		if (authentication.isAuthenticated()) {
			StudentAffairsOffice studentAffairsOffice = service.update(id, dto);
			if(studentAffairsOffice != null) {
				return new ResponseEntity<StudentAffairsOfficeDto>(mapper.toDto(studentAffairsOffice), HttpStatus.OK);
			}
		}
		return new ResponseEntity<StudentAffairsOfficeDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<StudentAffairsOffice> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<StudentAffairsOffice>(HttpStatus.OK);
		}
		return new ResponseEntity<StudentAffairsOffice>(HttpStatus.NOT_FOUND);
	}
}
