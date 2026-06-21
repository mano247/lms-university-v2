package com.lmsuniversity.finalthesis;

import java.util.ArrayList;
import java.util.List;
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

@Controller
@RequestMapping(path = "/api/final-thesis")
public class FinalThesisController {
	@Autowired
	private FinalThesisService service;

	@Autowired
	private FinalThesisMapper mapper;

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<FinalThesisDto>> getAll(){
		List<FinalThesisDto> finalTheses = mapper.toDtoList(service.findAll());
		return new ResponseEntity<List<FinalThesisDto>>(finalTheses, HttpStatus.OK);
		}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FinalThesisDto> get(@PathVariable("id") Long id) {
		Optional<FinalThesis> z = service.findOne(id);
		if(z.isPresent()) {
			return new ResponseEntity<FinalThesisDto>(mapper.toDto(z.get()), HttpStatus.OK);
		}
		return new ResponseEntity<FinalThesisDto>(HttpStatus.NOT_FOUND);

	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/by-student/{id}", method = RequestMethod.GET)
	public ResponseEntity<FinalThesisDto> findByStudent(@PathVariable("id") Long id){
		for (FinalThesis z : service.findAll()) {
			if (id.equals(z.getStudent().getId())) {
				return new ResponseEntity<FinalThesisDto>(mapper.toDto(z), HttpStatus.OK);
		}
			}
		return new ResponseEntity<FinalThesisDto>(HttpStatus.NOT_FOUND);
		}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/by-mentor/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<FinalThesisDto>> findByMentor(@PathVariable("id") Long id){
		List<FinalThesisDto> finalTheses = new ArrayList<>();
		for (FinalThesis z : service.findAll()) {
			if (id.equals(z.getMentor().getId())) {
				finalTheses.add(mapper.toDto(z));
		}
			}
		return new ResponseEntity<List<FinalThesisDto>>(finalTheses, HttpStatus.OK);
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
	public ResponseEntity<FinalThesisDto> update(@PathVariable("id") Long id, @Valid @RequestBody FinalThesisUpdateDto dto){
		FinalThesis finalThesis = service.update(id, dto);
		if(finalThesis != null) {
			return new ResponseEntity<FinalThesisDto>(mapper.toDto(finalThesis), HttpStatus.OK);
		}
		return new ResponseEntity<FinalThesisDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<FinalThesisDto> create(@Valid @RequestBody FinalThesisCreateDto dto){
		if (service.existsByStudentId(dto.getStudentId())) {
			return new ResponseEntity<FinalThesisDto>(HttpStatus.CONFLICT);
		}

		FinalThesis finalThesis = service.create(dto);
		return new ResponseEntity<FinalThesisDto>(mapper.toDto(finalThesis), HttpStatus.CREATED);
	}

}
