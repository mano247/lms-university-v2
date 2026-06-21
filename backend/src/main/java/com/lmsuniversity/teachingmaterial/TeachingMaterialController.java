package com.lmsuniversity.teachingmaterial;

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
@RequestMapping(path = "/api/teaching-materials")
public class TeachingMaterialController {
	@Autowired
	private TeachingMaterialService service;

	@Autowired
	private TeachingMaterialMapper mapper;

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<TeachingMaterialDto>> getAll(){
		List<TeachingMaterialDto> teachingMaterials = mapper.toDtoList(service.findAll());
		return new ResponseEntity<List<TeachingMaterialDto>>(teachingMaterials, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TeachingMaterialDto> get(@PathVariable("id") Long id){
		Optional<TeachingMaterial> nm = service.findOne(id);
		if(nm.isPresent()) {
			return new ResponseEntity<TeachingMaterialDto>(mapper.toDto(nm.get()), HttpStatus.OK);
		}
		return new ResponseEntity<TeachingMaterialDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<TeachingMaterialDto> create(@Valid @RequestBody TeachingMaterialCreateDto dto){
		TeachingMaterial teachingMaterial = service.create(dto);
		return new ResponseEntity<TeachingMaterialDto>(mapper.toDto(teachingMaterial), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<TeachingMaterialDto> update(@PathVariable("id") Long id, @Valid @RequestBody TeachingMaterialUpdateDto dto){
		TeachingMaterial teachingMaterial = service.update(id, dto);
		if(teachingMaterial != null) {
			return new ResponseEntity<TeachingMaterialDto>(mapper.toDto(teachingMaterial), HttpStatus.OK);
		}
		return new ResponseEntity<TeachingMaterialDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<TeachingMaterial> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<TeachingMaterial>(HttpStatus.OK);
		}
		return new ResponseEntity<TeachingMaterial>(HttpStatus.NOT_FOUND);
	}
}
