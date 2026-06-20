package com.lmsuniversity.faculty;

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

import com.lmsuniversity.rectorate.UniversityDto;

@Controller
@RequestMapping(path = "/api/faculties")
public class FacultyController {
	@Autowired
	private FacultyService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<FacultyDto>> getAll(){
		HashSet<FacultyDto> faculties = new HashSet<FacultyDto>();
		for (Faculty f : service.findAll()) {
			faculties.add(FacultyDto.builder()
					.id(f.getId())
					.facultyCode(f.getFacultyCode())
					.name(f.getName())
					.contact(f.getContact())
					.description(f.getDescription())
					.dean(f.getDean())
					.image(f.getImage())
					.address(f.getAddress())
					.university(UniversityDto.builder().id(f.getUniversity().getId()).name(f.getUniversity().getName()).build())
					.build());
		}
		return new ResponseEntity<Iterable<FacultyDto>>(faculties, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FacultyDto> get(@PathVariable("id") Long id){
		Optional<Faculty> f = service.findOne(id);
		if(f.isPresent()) {
			FacultyDto dto = FacultyDto.builder()
					.id(f.get().getId())
					.facultyCode(f.get().getFacultyCode())
					.name(f.get().getName())
					.contact(f.get().getContact())
					.description(f.get().getDescription())
					.dean(f.get().getDean())
					.image(f.get().getImage())
					.address(f.get().getAddress())
					.university(UniversityDto.builder().id(f.get().getUniversity().getId()).name(f.get().getUniversity().getName()).build())
					.build();
			return new ResponseEntity<FacultyDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<FacultyDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/code/{facultyCode}", method = RequestMethod.GET)
	public ResponseEntity<FacultyDto> get(@PathVariable("facultyCode") String facultyCode){
		Optional<Faculty> f = service.findByCode(facultyCode);
		if(f.isPresent()) {
			FacultyDto dto = FacultyDto.builder()
					.id(f.get().getId())
					.facultyCode(f.get().getFacultyCode())
					.name(f.get().getName())
					.contact(f.get().getContact())
					.description(f.get().getDescription())
					.dean(f.get().getDean())
					.image(f.get().getImage())
					.address(f.get().getAddress())
					.university(UniversityDto.builder().id(f.get().getUniversity().getId()).name(f.get().getUniversity().getName()).build())
					.build();
			return new ResponseEntity<FacultyDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<FacultyDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Faculty> create(@Valid @RequestBody Faculty r){
		try {
			service.save(r);
			return new ResponseEntity<Faculty>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Faculty>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Faculty> update(@PathVariable("id") Long id, @Valid @RequestBody Faculty faculty){
		Faculty u = service.findOne(id).orElse(null);

		if(u != null) {
			faculty.setId(id);
			faculty = service.save(faculty);

			return new ResponseEntity<Faculty>(HttpStatus.OK);
		}
		return new ResponseEntity<Faculty>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Faculty> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<Faculty>(HttpStatus.OK);
		}
		return new ResponseEntity<Faculty>(HttpStatus.NOT_FOUND);
	}
}
