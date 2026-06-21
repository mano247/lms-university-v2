package com.lmsuniversity.officesupply;

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
@RequestMapping(path = "/api/office-supplies")
@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
public class OfficeSupplyController {
	@Autowired
	private OfficeSupplyService service;

	@Autowired
	private OfficeSupplyMapper mapper;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Page<OfficeSupplyDto>> getAll(Pageable pageable){
		Page<OfficeSupplyDto> officeSupplies = service.findAll(pageable).map(mapper::toDto);
		return new ResponseEntity<Page<OfficeSupplyDto>>(officeSupplies, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<OfficeSupplyDto> get(@PathVariable("id") Long id){
		Optional<OfficeSupply> k = service.findOne(id);
		if(k.isPresent()) {
			return new ResponseEntity<OfficeSupplyDto>(mapper.toDto(k.get()), HttpStatus.OK);
		}
		return new ResponseEntity<OfficeSupplyDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<OfficeSupplyDto> create(@Valid @RequestBody OfficeSupplyCreateDto dto){
		OfficeSupply officeSupply = service.create(dto);
		return new ResponseEntity<OfficeSupplyDto>(mapper.toDto(officeSupply), HttpStatus.CREATED);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<OfficeSupplyDto> update(@PathVariable("id") Long id, @Valid @RequestBody OfficeSupplyUpdateDto dto){
		OfficeSupply officeSupply = service.update(id, dto);
		if(officeSupply != null) {
			return new ResponseEntity<OfficeSupplyDto>(mapper.toDto(officeSupply), HttpStatus.OK);
		}
		return new ResponseEntity<OfficeSupplyDto>(HttpStatus.NOT_FOUND);
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
