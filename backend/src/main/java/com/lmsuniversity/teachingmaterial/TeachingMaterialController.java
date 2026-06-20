package com.lmsuniversity.teachingmaterial;

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

@Controller
@RequestMapping(path = "/api/teaching-materials")
public class TeachingMaterialController {
	@Autowired
	private TeachingMaterialService service;

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<TeachingMaterialDto>> getAll(){
		HashSet<TeachingMaterialDto> teachingMaterials = new HashSet<TeachingMaterialDto>();
		for (TeachingMaterial nm : service.findAll()) {
			teachingMaterials.add(TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).publicationYear(nm.getPublicationYear()).publisher(nm.getPublisher()).description(nm.getDescription()).url(nm.getUrl()).outcome(nm.getOutcome()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build());
			
		}
		return new ResponseEntity<Iterable<TeachingMaterialDto>>(teachingMaterials, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TeachingMaterialDto> get(@PathVariable("id") Long id){
		Optional<TeachingMaterial> nm = service.findOne(id);
		if(nm.isPresent()) {
			TeachingMaterialDto dto = TeachingMaterialDto.builder()
					.id(nm.get().getId())
					.title(nm.get().getTitle())
					.authors(nm.get().getAuthors())
					.publicationYear(nm.get().getPublicationYear())
					.publisher(nm.get().getPublisher())
					.description(nm.get().getDescription())
					.url(nm.get().getUrl())
					.outcome(nm.get().getOutcome())
					.quantity(nm.get().getQuantity())
					.issuedQuantity(nm.get().getIssuedQuantity())
					.build();
			return new ResponseEntity<TeachingMaterialDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<TeachingMaterialDto>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<TeachingMaterial> create(@Valid @RequestBody TeachingMaterial r){
		try {
			service.save(r);
			return new ResponseEntity<TeachingMaterial>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<TeachingMaterial>(HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<TeachingMaterial> update(@PathVariable("id") Long id, @Valid @RequestBody TeachingMaterial teachingMaterial){
		TeachingMaterial u = service.findOne(id).orElse(null);
		if(u != null) {
			teachingMaterial.setId(id);
			teachingMaterial = service.save(teachingMaterial);
			return new ResponseEntity<TeachingMaterial>(teachingMaterial, HttpStatus.OK);
		}
		return new ResponseEntity<TeachingMaterial>(HttpStatus.NOT_FOUND);
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

