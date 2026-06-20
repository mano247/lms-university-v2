package com.lmsuniversity.user;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(path = "/api/administratori")
@CrossOrigin(origins = "http://localhost:4200")
public class AdministratorController {
	@Autowired
	private AdministratorService service;
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Administrator>> getAll(){
		HashSet<Administrator> administrators = new HashSet<Administrator>();
		for (Administrator a : service.findAll()) {
			administrators.add(new Administrator(a.getId(),a.getFirstName(),a.getLastName(), a.getUsername(), a.getEmail(),a.getPassword(), a.getPermissions()));
		}
		return new ResponseEntity<Iterable<Administrator>>(administrators, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Administrator> get(@PathVariable("id") Long id){
		Optional<Administrator> a = service.findOne(id);
		if(a.isPresent()) {
			Administrator administrator = new Administrator(a.get().getId(), a.get().getFirstName(),
					a.get().getLastName(),
					a.get().getUsername(),
					a.get().getEmail(),
					a.get().getPassword(),   
					a.get().getPermissions());
			return new ResponseEntity<Administrator>(administrator, HttpStatus.OK);
		}
		return new ResponseEntity<Administrator>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Administrator> create(@RequestBody Administrator r){
		try {
			service.save(r);
			return new ResponseEntity<Administrator>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Administrator>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Administrator> update(@PathVariable("id") Long id, @RequestBody Administrator administrator, Authentication authentication){
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
	
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Administrator> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<Administrator>(HttpStatus.OK);
		}
		return new ResponseEntity<Administrator>(HttpStatus.NOT_FOUND);
	}


}
