package com.lmsuniversity.teachingmaterial;

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

@Controller
@RequestMapping(path = "/api/nastavnimaterijal")
@CrossOrigin(origins = "http://localhost:4200")
public class TeachingMaterialController {
	@Autowired
	private TeachingMaterialService service;
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<TeachingMaterialDto>> getAll(){
		HashSet<TeachingMaterialDto> teachingMaterials = new HashSet<TeachingMaterialDto>();
		for (TeachingMaterial nm : service.findAll()) {
			teachingMaterials.add(new TeachingMaterialDto(
					nm.getId(), 
					nm.getTitle(), 
					nm.getAuthors(), 
					nm.getPublicationYear(), 
					nm.getPublisher(), 
					nm.getDescription(), 
					nm.getUrl(), 
					nm.getOutcome(),
					nm.getQuantity(),
					nm.getIssuedQuantity()));
			
		}
		return new ResponseEntity<Iterable<TeachingMaterialDto>>(teachingMaterials, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TeachingMaterialDto> get(@PathVariable("id") Long id){
		Optional<TeachingMaterial> nm = service.findOne(id);
		if(nm.isPresent()) {
			TeachingMaterialDto dto = new  TeachingMaterialDto(
					nm.get().getId(), 
					nm.get().getTitle(), 
					nm.get().getAuthors(), 
					nm.get().getPublicationYear(), 
					nm.get().getPublisher(), 
					nm.get().getDescription(), 
					nm.get().getUrl(), 
					nm.get().getOutcome(),
					nm.get().getQuantity(),
					nm.get().getIssuedQuantity());
			return new ResponseEntity<TeachingMaterialDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<TeachingMaterialDto>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<TeachingMaterial> create(@RequestBody TeachingMaterial r){
		try {
			service.save(r);
			return new ResponseEntity<TeachingMaterial>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<TeachingMaterial>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<TeachingMaterial> update(@PathVariable("id") Long id, @RequestBody TeachingMaterial teachingMaterial){
		TeachingMaterial u = service.findOne(id).orElse(null);
		if(u != null) {
			teachingMaterial.setId(id);
			teachingMaterial = service.save(teachingMaterial);
			return new ResponseEntity<TeachingMaterial>(teachingMaterial, HttpStatus.OK);
		}
		return new ResponseEntity<TeachingMaterial>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<TeachingMaterial> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<TeachingMaterial>(HttpStatus.OK);
		}
		return new ResponseEntity<TeachingMaterial>(HttpStatus.NOT_FOUND);
	}
}

