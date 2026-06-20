package com.lmsuniversity.officesupply;

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
@RequestMapping(path = "/api/office-supplies")
@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
public class OfficeSupplyController {
	@Autowired
	private OfficeSupplyService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<OfficeSupplyDto>> getAll(){
		HashSet<OfficeSupplyDto> officeSupplies = new HashSet<OfficeSupplyDto>();
		for (OfficeSupply k : service.findAll()) {
			officeSupplies.add(new OfficeSupplyDto(k.getId(),k.getName(),k.getQuantity(),k.getIssuedQuantity()));
		}
		return new ResponseEntity<Iterable<OfficeSupplyDto>>(officeSupplies, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<OfficeSupplyDto> get(@PathVariable("id") Long id){
		Optional<OfficeSupply> k = service.findOne(id);
		if(k.isPresent()) {
			OfficeSupplyDto dto = new OfficeSupplyDto(k.get().getId(),k.get().getName(),k.get().getQuantity(),k.get().getIssuedQuantity());
			return new ResponseEntity<OfficeSupplyDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<OfficeSupplyDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<OfficeSupply> create(@Valid @RequestBody OfficeSupply r){
		try {
			service.save(r);
			return new ResponseEntity<OfficeSupply>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<OfficeSupply>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<OfficeSupply> update(@PathVariable("id") Long id, @Valid @RequestBody OfficeSupply officeSupply){
		OfficeSupply u = service.findOne(id).orElse(null);

		if(u != null) {
			officeSupply.setId(id);
			officeSupply = service.save(officeSupply);
			return new ResponseEntity<OfficeSupply>(officeSupply, HttpStatus.OK);
		}
		return new ResponseEntity<OfficeSupply>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<OfficeSupply> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<OfficeSupply>(HttpStatus.OK);
		}
		return new ResponseEntity<OfficeSupply>(HttpStatus.NOT_FOUND);
	}
}
