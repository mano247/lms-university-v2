package com.lmsuniversity.finalthesis;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lmsuniversity.user.TeacherDto;
import com.lmsuniversity.user.StudentDto;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.Student;

@Controller
@RequestMapping(path = "/api/final-thesis")
public class FinalThesisController {
	@Autowired
	private FinalThesisService service;

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<FinalThesisDto>> getAll(){
		HashSet<FinalThesisDto> finalTheses = new HashSet<FinalThesisDto>();
		for (FinalThesis z : service.findAll()) {
			Student st = z.getStudent();
			Teacher n = z.getMentor();

			StudentDto student = StudentDto.builder().id(st.getId()).firstName(st.getFirstName()).lastName(st.getLastName()).email(st.getEmail()).password(st.getPassword()).indexNumber(st.getIndexNumber()).username(st.getUsername()).build();
			TeacherDto mentor = TeacherDto.builder().id(n.getId()).userType(null).firstName(n.getFirstName()).lastName(n.getLastName()).email(n.getEmail()).build();

			finalTheses.add(FinalThesisDto.builder().id(z.getId()).topic(z.getTopic()).link(z.getLink()).student(student).mentor(mentor).build());
		}
		return new ResponseEntity<Iterable<FinalThesisDto>>(finalTheses, HttpStatus.OK);
		}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FinalThesisDto> get(@PathVariable("id") Long id) {
		Optional<FinalThesis> z = service.findOne(id);
		if(z.isPresent()) {
			Student st = z.get().getStudent();
			Teacher n = z.get().getMentor();

			StudentDto student = StudentDto.builder().id(st.getId()).firstName(st.getFirstName()).lastName(st.getLastName()).email(st.getEmail()).password(st.getPassword()).indexNumber(st.getIndexNumber()).username(st.getUsername()).build();
			TeacherDto mentor = TeacherDto.builder().id(n.getId()).userType(null).firstName(n.getFirstName()).lastName(n.getLastName()).email(n.getEmail()).build();


			FinalThesisDto finalThesis = FinalThesisDto.builder().id(z.get().getId()).topic(z.get().getTopic()).link(z.get().getLink()).student(student).mentor(mentor).build();
			return new ResponseEntity<FinalThesisDto>(finalThesis, HttpStatus.OK);
		}
		return new ResponseEntity<FinalThesisDto>(HttpStatus.NOT_FOUND);

	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/by-student/{id}", method = RequestMethod.GET)
	public ResponseEntity<FinalThesisDto> findByStudent(@PathVariable("id") Long id){
		for (FinalThesis z : service.findAll()) {
			Student st = z.getStudent();
			if (id == st.getId()) {
				Teacher n = z.getMentor();

				StudentDto student = StudentDto.builder().id(st.getId()).firstName(st.getFirstName()).lastName(st.getLastName()).email(st.getEmail()).password(st.getPassword()).indexNumber(st.getIndexNumber()).username(st.getUsername()).build();
				TeacherDto mentor = TeacherDto.builder().id(n.getId()).userType(null).firstName(n.getFirstName()).lastName(n.getLastName()).email(n.getEmail()).build();

				FinalThesisDto finalThesis = FinalThesisDto.builder().id(z.getId()).topic(z.getTopic()).link(z.getLink()).student(student).mentor(mentor).build();
				return new ResponseEntity<FinalThesisDto>(finalThesis, HttpStatus.OK);
		}
			}
		return new ResponseEntity<FinalThesisDto>(HttpStatus.NOT_FOUND);
		}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/by-mentor/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<FinalThesisDto>> findByMentor(@PathVariable("id") Long id){
		HashSet<FinalThesisDto> finalTheses = new HashSet<FinalThesisDto>();
		for (FinalThesis z : service.findAll()) {
			Teacher n = z.getMentor();
			if (id == n.getId()) {
				Student st = z.getStudent();

				StudentDto student = StudentDto.builder().id(st.getId()).firstName(st.getFirstName()).lastName(st.getLastName()).email(st.getEmail()).password(st.getPassword()).indexNumber(st.getIndexNumber()).username(st.getUsername()).build();
				TeacherDto mentor = TeacherDto.builder().id(n.getId()).userType(null).firstName(n.getFirstName()).lastName(n.getLastName()).email(n.getEmail()).build();

				finalTheses.add(FinalThesisDto.builder().id(z.getId()).topic(z.getTopic()).link(z.getLink()).student(student).mentor(mentor).build());
		}
			}
		return new ResponseEntity<Iterable<FinalThesisDto>>(finalTheses, HttpStatus.OK);
		}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<FinalThesis> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<FinalThesis>(HttpStatus.OK);
		}
		return new ResponseEntity<FinalThesis>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<FinalThesis> update(@PathVariable("id") Long id, @Valid @RequestBody FinalThesis finalThesis){
		FinalThesis existing = service.findOne(id).orElse(null);

		if(existing != null) {
			finalThesis.setId(id);
			finalThesis = service.save(finalThesis);
			return new ResponseEntity<FinalThesis>(finalThesis, HttpStatus.OK);
				}
		return new ResponseEntity<FinalThesis>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<FinalThesis> create(@Valid @RequestBody FinalThesis finalThesis){
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
