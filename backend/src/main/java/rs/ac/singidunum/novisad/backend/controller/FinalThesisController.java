package rs.ac.singidunum.novisad.backend.controller;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rs.ac.singidunum.novisad.backend.dto.TeacherDTO;
import rs.ac.singidunum.novisad.backend.dto.StudentDTO;
import rs.ac.singidunum.novisad.backend.dto.FinalThesisDTO;
import rs.ac.singidunum.novisad.backend.model.FinalThesis;
import rs.ac.singidunum.novisad.backend.model.user.Teacher;
import rs.ac.singidunum.novisad.backend.model.user.Student;
import rs.ac.singidunum.novisad.backend.service.FinalThesisService;

@Controller
@RequestMapping(path = "/api/zavrsniRad")
@CrossOrigin(origins = "http://localhost:4200")
public class FinalThesisController {
	@Autowired
	private FinalThesisService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<FinalThesisDTO>> getAll(){
		HashSet<FinalThesisDTO> finalTheses = new HashSet<FinalThesisDTO>();
		for (FinalThesis z : service.findAll()) {
			Student st = z.getStudent();
			Teacher n = z.getMentor();

			StudentDTO student = new StudentDTO(st.getId(),st.getFirstName(),st.getLastName(),st.getEmail(),st.getPassword(),st.getIndexNumber(),st.getUsername());
			TeacherDTO mentor = new TeacherDTO(n.getId(),null,n.getFirstName(),n.getLastName(),n.getEmail(),n.getPassword());

			finalTheses.add(new FinalThesisDTO(z.getId(),z.getTopic(),z.getLink(),student,mentor));
		}
		return new ResponseEntity<Iterable<FinalThesisDTO>>(finalTheses, HttpStatus.OK);
		}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FinalThesisDTO> get(@PathVariable("id") Long id) {
		Optional<FinalThesis> z = service.findOne(id);
		if(z.isPresent()) {
			Student st = z.get().getStudent();
			Teacher n = z.get().getMentor();

			StudentDTO student = new StudentDTO(st.getId(),st.getFirstName(),st.getLastName(),st.getEmail(),st.getPassword(),st.getIndexNumber(),st.getUsername());
			TeacherDTO mentor = new TeacherDTO(n.getId(),null,n.getFirstName(),n.getLastName(),n.getEmail(),n.getPassword());


			FinalThesisDTO finalThesis = new FinalThesisDTO(z.get().getId(),z.get().getTopic(),z.get().getLink(),student,mentor);
			return new ResponseEntity<FinalThesisDTO>(finalThesis, HttpStatus.OK);
		}
		return new ResponseEntity<FinalThesisDTO>(HttpStatus.NOT_FOUND);

	}

	@RequestMapping(path = "/fbs/{id}", method = RequestMethod.GET)
	public ResponseEntity<FinalThesisDTO> findByStudent(@PathVariable("id") Long id){
		for (FinalThesis z : service.findAll()) {
			Student st = z.getStudent();
			if (id == st.getId()) {
				Teacher n = z.getMentor();

				StudentDTO student = new StudentDTO(st.getId(),st.getFirstName(),st.getLastName(),st.getEmail(),st.getPassword(),st.getIndexNumber(),st.getUsername());
				TeacherDTO mentor = new TeacherDTO(n.getId(),null,n.getFirstName(),n.getLastName(),n.getEmail(),n.getPassword());

				FinalThesisDTO finalThesis = new FinalThesisDTO(z.getId(),z.getTopic(),z.getLink(),student,mentor);
				return new ResponseEntity<FinalThesisDTO>(finalThesis, HttpStatus.OK);
		}
			}
		return new ResponseEntity<FinalThesisDTO>(HttpStatus.NOT_FOUND);
		}

	@RequestMapping(path = "/fbm/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<FinalThesisDTO>> findByMentor(@PathVariable("id") Long id){
		HashSet<FinalThesisDTO> finalTheses = new HashSet<FinalThesisDTO>();
		for (FinalThesis z : service.findAll()) {
			Teacher n = z.getMentor();
			if (id == n.getId()) {
				Student st = z.getStudent();

				StudentDTO student = new StudentDTO(st.getId(),st.getFirstName(),st.getLastName(),st.getEmail(),st.getPassword(),st.getIndexNumber(),st.getUsername());
				TeacherDTO mentor = new TeacherDTO(n.getId(),null,n.getFirstName(),n.getLastName(),n.getEmail(),n.getPassword());

				finalTheses.add(new FinalThesisDTO(z.getId(),z.getTopic(),z.getLink(),student,mentor));
		}
			}
		return new ResponseEntity<Iterable<FinalThesisDTO>>(finalTheses, HttpStatus.OK);
		}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<FinalThesis> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<FinalThesis>(HttpStatus.OK);
		}
		return new ResponseEntity<FinalThesis>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<FinalThesis> update(@PathVariable("id") Long id, @RequestBody FinalThesis finalThesis){
		FinalThesis existing = service.findOne(id).orElse(null);

		if(existing != null) {
			finalThesis.setId(id);
			finalThesis = service.save(finalThesis);
			return new ResponseEntity<FinalThesis>(finalThesis, HttpStatus.OK);
				}
		return new ResponseEntity<FinalThesis>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<FinalThesis> create(@RequestBody FinalThesis finalThesis){
		for (FinalThesis z : service.findAll()) {
			if(z.getStudent().getId().equals(finalThesis.getStudent().getId())) {
				return new ResponseEntity<FinalThesis>(HttpStatus.CONFLICT);
			}
		}

		try {
			service.save(finalThesis);
			return new ResponseEntity<FinalThesis>(finalThesis, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<FinalThesis>(HttpStatus.BAD_REQUEST);
	}

}
