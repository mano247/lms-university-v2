package com.lmsuniversity.user;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private PasswordEncoder encoder;

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<StudentAffairsOffice>> getAll(){
		HashSet<StudentAffairsOffice> studentAffairsOffices = new HashSet<StudentAffairsOffice>();
		for (StudentAffairsOffice s : service.findAll()) {
			studentAffairsOffices.add(StudentAffairsOffice.builder().id(s.getId()).firstName(s.getFirstName()).lastName(s.getLastName()).username(s.getUsername()).email(s.getEmail()).password(s.getPassword()).permissions(s.getPermissions()).build());
		}
		return new ResponseEntity<Iterable<StudentAffairsOffice>>(studentAffairsOffices, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<StudentAffairsOffice> get(@PathVariable("id") Long id){
		Optional<StudentAffairsOffice> s = service.findOne(id);
		if(s.isPresent()) {
			StudentAffairsOffice studentAffairsOffice = StudentAffairsOffice.builder()
					.id(s.get().getId())
					.firstName(s.get().getFirstName())
					.lastName(s.get().getLastName())
					.username(s.get().getUsername())
					.email(s.get().getEmail())
					.password(s.get().getPassword())
					.permissions(s.get().getPermissions())
					.build();
			return new ResponseEntity<StudentAffairsOffice>(studentAffairsOffice, HttpStatus.OK);
		}
		return new ResponseEntity<StudentAffairsOffice>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<StudentAffairsOffice> create(@Valid @RequestBody StudentAffairsOffice r){
		try {
			r.setPassword(encoder.encode(r.getPassword()));
			service.save(r);
			return new ResponseEntity<StudentAffairsOffice>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<StudentAffairsOffice>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION','STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<StudentAffairsOffice> update(@PathVariable("id") Long id, @Valid @RequestBody StudentAffairsOffice studentAffairsOffice, Authentication authentication){
		if (authentication.isAuthenticated()) {
				StudentAffairsOffice u = service.findOne(id).orElse(null);
				if(u != null) {
					studentAffairsOffice.setId(id);
					studentAffairsOffice.setPermissions(u.getPermissions());
					studentAffairsOffice.setPassword(u.getPassword());
					studentAffairsOffice = service.save(studentAffairsOffice);
					return new ResponseEntity<StudentAffairsOffice>(studentAffairsOffice, HttpStatus.OK);
				}
		}
		return new ResponseEntity<StudentAffairsOffice>(HttpStatus.NOT_FOUND);
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
