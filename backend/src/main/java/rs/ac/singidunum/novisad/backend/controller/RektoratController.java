package rs.ac.singidunum.novisad.backend.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

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

import rs.ac.singidunum.novisad.backend.dto.RectorateDTO;
import rs.ac.singidunum.novisad.backend.dto.UniversityDTO;
import rs.ac.singidunum.novisad.backend.model.Rectorate;
import rs.ac.singidunum.novisad.backend.service.RektoratService;

@Controller
@RequestMapping(path = "/api/rektorati")
@CrossOrigin(origins = "http://localhost:4200")
public class RektoratController {
	@Autowired
	private RektoratService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<RectorateDTO>> getAll(){
		HashSet<RectorateDTO> rectorate = new HashSet<RectorateDTO>();
		for (Rectorate r : service.findAll()) {
			Set<UniversityDTO> universities = r.getUniversities().stream().
					map(university -> new UniversityDTO(university.getId(), university.getName(), university.getFoundingDate(), university.getContact(), university.getAddress() , university.getDescription(), university.getImage())).
					collect(Collectors.toSet());
			rectorate.add(new RectorateDTO(r.getId(), r.getName(), r.getContact(), r.getImage(), r.getAddress(),r.getRectorName(), universities));
		}
		return new ResponseEntity<Iterable<RectorateDTO>>(rectorate, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<RectorateDTO> get(@PathVariable("id") Long id){
		Optional<Rectorate> r = service.findOne(id);
		if(r.isPresent()) {
			Set<UniversityDTO> universities = r.get().getUniversities().stream().
					map(university -> new UniversityDTO(university.getId(), university.getName(), university.getFoundingDate(), university.getContact(), university.getAddress() , university.getDescription(), university.getImage())).
					collect(Collectors.toSet());
			RectorateDTO dto = new RectorateDTO(r.get().getId(), r.get().getName(), r.get().getContact(), r.get().getImage(), r.get().getAddress(),r.get().getRectorName(), universities);
			return new ResponseEntity<RectorateDTO>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<RectorateDTO>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Rectorate> create(@RequestBody Rectorate r){
		try {
			service.save(r);
			return new ResponseEntity<Rectorate>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Rectorate>(HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Rectorate> update(@PathVariable("id") Long id, @RequestBody Rectorate rectorate){
		Rectorate r = service.findOne(id).orElse(null);
		if(r != null) {
			rectorate.setId(id);
			rectorate = service.save(rectorate);
			return new ResponseEntity<Rectorate>(rectorate, HttpStatus.OK);
		}
		return new ResponseEntity<Rectorate>(HttpStatus.NOT_FOUND);
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
