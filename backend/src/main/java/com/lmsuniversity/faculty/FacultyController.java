package com.lmsuniversity.faculty;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lmsuniversity.rectorate.UniversityDto;

@Controller
@RequestMapping(path = "/api/faculties")
@CrossOrigin(origins = "http://localhost:4200")
public class FacultyController {
	@Autowired
	private FacultyService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<FacultyDto>> getAll(){
		HashSet<FacultyDto> faculties = new HashSet<FacultyDto>();
		for (Faculty f : service.findAll()) {
			faculties.add(new FacultyDto(f.getId(), f.getFacultyCode(), f.getName(), f.getContact(), f.getDescription(), f.getDean(), f.getImage(), f.getAddress(), new UniversityDto(f.getUniversity().getId(),f.getUniversity().getName())));
		}
		return new ResponseEntity<Iterable<FacultyDto>>(faculties, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FacultyDto> get(@PathVariable("id") Long id){
		Optional<Faculty> f = service.findOne(id);
		if(f.isPresent()) {
			FacultyDto dto = new FacultyDto(f.get().getId(),f.get().getFacultyCode(),  f.get().getName(), f.get().getContact(), f.get().getDescription(), f.get().getDean(), f.get().getImage(), f.get().getAddress(), new UniversityDto(f.get().getUniversity().getId(),f.get().getUniversity().getName()));
			return new ResponseEntity<FacultyDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<FacultyDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/s/{facultyCode}", method = RequestMethod.GET)
	public ResponseEntity<FacultyDto> get(@PathVariable("facultyCode") String facultyCode){
		Optional<Faculty> f = service.findByCode(facultyCode);
		if(f.isPresent()) {
			FacultyDto dto = new FacultyDto(f.get().getId(),f.get().getFacultyCode(),  f.get().getName(), f.get().getContact(), f.get().getDescription(), f.get().getDean(), f.get().getImage(), f.get().getAddress(), new UniversityDto(f.get().getUniversity().getId(),f.get().getUniversity().getName()));
			return new ResponseEntity<FacultyDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<FacultyDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Faculty> create(@RequestBody Faculty r){
		try {
			service.save(r);
			return new ResponseEntity<Faculty>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Faculty>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Faculty> update(@PathVariable("id") Long id, @RequestBody Faculty faculty){
		Faculty u = service.findOne(id).orElse(null);

		if(u != null) {
			faculty.setId(id);
			faculty = service.save(faculty);

			return new ResponseEntity<Faculty>(HttpStatus.OK);
		}
		return new ResponseEntity<Faculty>(HttpStatus.NOT_FOUND);
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
