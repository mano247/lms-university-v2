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

import rs.ac.singidunum.novisad.backend.dto.RectorateDTO;
import rs.ac.singidunum.novisad.backend.dto.UniversityDTO;
import rs.ac.singidunum.novisad.backend.model.academic.University;
import rs.ac.singidunum.novisad.backend.service.UniversityService;

@Controller
@RequestMapping(path = "/api/universities")
@CrossOrigin(origins = "http://localhost:4200")
public class UniversityController {

	@Autowired
	private UniversityService service;
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<UniversityDTO>> getAll(){
		HashSet<UniversityDTO> universities = new HashSet<UniversityDTO>();
		for (University u : service.findAll()) {
			RectorateDTO rectorate = new RectorateDTO(u.getRectorate().getId(), u.getRectorate().getName(),
					u.getRectorate().getContact(), u.getRectorate().getImage(), u.getRectorate().getAddress(),
					u.getRectorate().getRectorName());
			universities.add(new UniversityDTO(u.getId(), u.getName(), u.getFoundingDate(), u.getDescription() , u.getContact(),  u.getImage(), u.getAddress(), rectorate));
		}
		return new ResponseEntity<Iterable<UniversityDTO>>(universities, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UniversityDTO> get(@PathVariable("id") Long id){
		Optional<University> u = service.findOne(id);
		if(u.isPresent()) {
			RectorateDTO rectorate = new RectorateDTO(u.get().getRectorate().getId(), u.get().getRectorate().getName(),
					u.get().getRectorate().getContact(), u.get().getRectorate().getImage(), u.get().getRectorate().getAddress(),
					u.get().getRectorate().getRectorName());
			UniversityDTO dto = new UniversityDTO(u.get().getId(), u.get().getName(), u.get().getFoundingDate(), u.get().getContact(), u.get().getDescription(),  u.get().getImage(), u.get().getAddress(), rectorate);
			return new ResponseEntity<UniversityDTO>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<UniversityDTO>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<University> create(@RequestBody University r){
		try {
			service.save(r);
			return new ResponseEntity<University>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<University>(HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<University> update(@PathVariable("id") Long id, @RequestBody University university){
		University u = service.findOne(id).orElse(null);
		if(u != null) {
			university.setId(id);
			university.setRectorate(u.getRectorate());
			university = service.save(university);
			return new ResponseEntity<University>(university, HttpStatus.OK);
		}
		return new ResponseEntity<University>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<University> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<University>(HttpStatus.OK);
		}
		return new ResponseEntity<University>(HttpStatus.NOT_FOUND);
	}
}
