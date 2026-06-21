package com.lmsuniversity.studyprogram;

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
@RequestMapping(path = "/api/studyPrograms")
public class StudyProgramController {
	@Autowired
	private StudyProgramService service;

	@Autowired
	private StudyProgramMapper mapper;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<StudyProgramDto>> getAll(){
		List<StudyProgramDto> studyPrograms = mapper.toDtoList(service.findAll());
		return new ResponseEntity<List<StudyProgramDto>>(studyPrograms, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<StudyProgramDto> get(@PathVariable("id") Long id){
		Optional<StudyProgram> sp = service.findOne(id);
		if(sp.isPresent()) {
			return new ResponseEntity<StudyProgramDto>(mapper.toDto(sp.get()), HttpStatus.OK);
		}
		return new ResponseEntity<StudyProgramDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/code/{programCode}", method = RequestMethod.GET)
	public ResponseEntity<StudyProgramDto> get(@PathVariable("programCode") String programCode){
		Optional<StudyProgram> sp = service.findByProgramCode(programCode);
		if(sp.isPresent()) {
			return new ResponseEntity<StudyProgramDto>(mapper.toDto(sp.get()), HttpStatus.OK);
		}
		return new ResponseEntity<StudyProgramDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<StudyProgramDto> create(@Valid @RequestBody StudyProgramCreateDto dto){
		StudyProgram studyProgram = service.create(dto);
		return new ResponseEntity<StudyProgramDto>(mapper.toDto(studyProgram), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<StudyProgramDto> update(@PathVariable("id") Long id, @Valid @RequestBody StudyProgramUpdateDto dto){
		StudyProgram studyProgram = service.update(id, dto);
		if(studyProgram != null) {
			return new ResponseEntity<StudyProgramDto>(mapper.toDto(studyProgram), HttpStatus.OK);
		}
		return new ResponseEntity<StudyProgramDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<StudyProgram> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<StudyProgram>(HttpStatus.OK);
		}
		return new ResponseEntity<StudyProgram>(HttpStatus.NOT_FOUND);
	}
}
