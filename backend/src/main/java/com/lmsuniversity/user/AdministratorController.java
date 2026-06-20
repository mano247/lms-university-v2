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
@RequestMapping(path = "/api/administrators")
public class AdministratorController {
	@Autowired
	private AdministratorService service;

	@Autowired
	private PasswordEncoder encoder;

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Administrator>> getAll(){
		HashSet<Administrator> administrators = new HashSet<Administrator>();
		for (Administrator a : service.findAll()) {
			administrators.add(Administrator.builder().id(a.getId()).firstName(a.getFirstName()).lastName(a.getLastName()).username(a.getUsername()).email(a.getEmail()).password(a.getPassword()).permissions(a.getPermissions()).build());
		}
		return new ResponseEntity<Iterable<Administrator>>(administrators, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Administrator> get(@PathVariable("id") Long id){
		Optional<Administrator> a = service.findOne(id);
		if(a.isPresent()) {
			Administrator administrator = Administrator.builder()
					.id(a.get().getId())
					.firstName(a.get().getFirstName())
					.lastName(a.get().getLastName())
					.username(a.get().getUsername())
					.email(a.get().getEmail())
					.password(a.get().getPassword())
					.permissions(a.get().getPermissions())
					.build();
			return new ResponseEntity<Administrator>(administrator, HttpStatus.OK);
		}
		return new ResponseEntity<Administrator>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Administrator> create(@Valid @RequestBody Administrator r){
		try {
			r.setPassword(encoder.encode(r.getPassword()));
			service.save(r);
			return new ResponseEntity<Administrator>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Administrator>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Administrator> update(@PathVariable("id") Long id, @Valid @RequestBody Administrator administrator, Authentication authentication){
		if (authentication.isAuthenticated()) {

				Administrator u = service.findOne(id).orElse(null);
				if(u != null) {
					administrator.setId(id);
					administrator.setPassword(u.getPassword());
					administrator.setPermissions(u.getPermissions());
					administrator = service.save(administrator);
					return new ResponseEntity<Administrator>(administrator, HttpStatus.OK);
				}

			}

		return new ResponseEntity<Administrator>(HttpStatus.NOT_FOUND);
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
