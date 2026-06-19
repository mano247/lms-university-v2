package rs.ac.singidunum.novisad.backend.controller;

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

import rs.ac.singidunum.novisad.backend.dto.FacultyDTO;
import rs.ac.singidunum.novisad.backend.dto.UniversityDTO;
import rs.ac.singidunum.novisad.backend.model.academic.Faculty;
import rs.ac.singidunum.novisad.backend.service.FakultetService;


@Controller
@RequestMapping(path = "/api/faculties")
@CrossOrigin(origins = "http://localhost:4200")
public class FakultetController {
	@Autowired
	private FakultetService service;
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<FacultyDTO>> getAll(){
		HashSet<FacultyDTO> faculty = new HashSet<FacultyDTO>();
		for (Faculty f : service.findAll()) {
			faculty.add(new FacultyDTO(f.getId(), f.getFacultyCode(), f.getName(), f.getContact(), f.getDescription(), f.getDean(), f.getImage(), f.getAddress(), new UniversityDTO(f.getUniversity().getId(),f.getUniversity().getName())));
		}
		return new ResponseEntity<Iterable<FacultyDTO>>(faculty, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FacultyDTO> get(@PathVariable("id") Long id){
		Optional<Faculty> f = service.findOne(id);
		if(f.isPresent()) {
			FacultyDTO dto = new FacultyDTO(f.get().getId(),f.get().getFacultyCode(),  f.get().getName(), f.get().getContact(), f.get().getDescription(), f.get().getDean(), f.get().getImage(), f.get().getAddress(), new UniversityDTO(f.get().getUniversity().getId(),f.get().getUniversity().getName()));
			return new ResponseEntity<FacultyDTO>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<FacultyDTO>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = "/s/{facultyCode}", method = RequestMethod.GET)
	public ResponseEntity<FacultyDTO> get(@PathVariable("facultyCode") String facultyCode){
		Optional<Faculty> f = service.findBySifra(facultyCode);
		if(f.isPresent()) {
			FacultyDTO dto = new FacultyDTO(f.get().getId(),f.get().getFacultyCode(),  f.get().getName(), f.get().getContact(), f.get().getDescription(), f.get().getDean(), f.get().getImage(), f.get().getAddress(), new UniversityDTO(f.get().getUniversity().getId(),f.get().getUniversity().getName()));
			return new ResponseEntity<FacultyDTO>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<FacultyDTO>(HttpStatus.NOT_FOUND);
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

