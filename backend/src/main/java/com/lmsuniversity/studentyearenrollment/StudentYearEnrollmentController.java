package com.lmsuniversity.studentyearenrollment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "/api/enrollments")
@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
public class StudentYearEnrollmentController {
	@Autowired
	private StudentYearEnrollmentService service;

	@Autowired
	private StudentYearEnrollmentMapper mapper;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Page<StudentYearEnrollmentDto>> getAll(Pageable pageable){
		Page<StudentYearEnrollmentDto> enrollments = service.findAll(pageable).map(mapper::toDto);
		return new ResponseEntity<Page<StudentYearEnrollmentDto>>(enrollments, HttpStatus.OK);
		}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<StudentYearEnrollmentDto> get(@PathVariable("id") Long id) {
		Optional<StudentYearEnrollment> s = service.findOne(id);
		if(s.isPresent()) {
			return new ResponseEntity<StudentYearEnrollmentDto>(mapper.toDto(s.get()), HttpStatus.OK);
		}
		return new ResponseEntity<StudentYearEnrollmentDto>(HttpStatus.NOT_FOUND);

	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<StudentYearEnrollmentDto> create(@Valid @RequestBody StudentYearEnrollmentCreateDto dto){
		StudentYearEnrollment enrollment = service.create(dto);
		return new ResponseEntity<StudentYearEnrollmentDto>(mapper.toDto(enrollment), HttpStatus.CREATED);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<StudentYearEnrollment> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<StudentYearEnrollment>(HttpStatus.OK);
		}
		return new ResponseEntity<StudentYearEnrollment>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<StudentYearEnrollmentDto> update(@PathVariable("id") Long id, @Valid @RequestBody StudentYearEnrollmentUpdateDto dto){
		StudentYearEnrollment enrollment = service.update(id, dto);
		if(enrollment != null) {
			return new ResponseEntity<StudentYearEnrollmentDto>(mapper.toDto(enrollment), HttpStatus.OK);
		}
		return new ResponseEntity<StudentYearEnrollmentDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/by-student/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<StudentYearEnrollmentDto>> getByStudentId(@PathVariable("id") Long id){
		List<StudentYearEnrollmentDto> enrollments = service.findByStudentId(id).stream().map(mapper::toDto).toList();
		return new ResponseEntity<List<StudentYearEnrollmentDto>>(enrollments, HttpStatus.OK);
		}
}
