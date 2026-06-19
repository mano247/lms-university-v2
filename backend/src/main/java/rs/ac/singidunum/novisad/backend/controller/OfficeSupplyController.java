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

import rs.ac.singidunum.novisad.backend.dto.OfficeSupplyDTO;
import rs.ac.singidunum.novisad.backend.model.OfficeSupply;
import rs.ac.singidunum.novisad.backend.service.OfficeSupplyService;

@Controller
@RequestMapping(path = "/api/kancelariskiMaterial")
@CrossOrigin(origins = "http://localhost:4200")
public class OfficeSupplyController {
	@Autowired
	private OfficeSupplyService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<OfficeSupplyDTO>> getAll(){
		HashSet<OfficeSupplyDTO> officeSupplies = new HashSet<OfficeSupplyDTO>();
		for (OfficeSupply k : service.findAll()) {
			officeSupplies.add(new OfficeSupplyDTO(k.getId(),k.getName(),k.getQuantity(),k.getIssuedQuantity()));
		}
		return new ResponseEntity<Iterable<OfficeSupplyDTO>>(officeSupplies, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<OfficeSupplyDTO> get(@PathVariable("id") Long id){
		Optional<OfficeSupply> k = service.findOne(id);
		if(k.isPresent()) {
			OfficeSupplyDTO dto = new OfficeSupplyDTO(k.get().getId(),k.get().getName(),k.get().getQuantity(),k.get().getIssuedQuantity());
			return new ResponseEntity<OfficeSupplyDTO>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<OfficeSupplyDTO>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<OfficeSupply> create(@RequestBody OfficeSupply r){
		try {
			service.save(r);
			return new ResponseEntity<OfficeSupply>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<OfficeSupply>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<OfficeSupply> update(@PathVariable("id") Long id, @RequestBody OfficeSupply officeSupply){
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
