package com.lmsuniversity.studyprogram;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lmsuniversity.faculty.FacultyDto;


@Controller
@RequestMapping(path = "/api/studyPrograms")
@CrossOrigin(origins = "http://localhost:4200")
public class StudyProgramController {
	@Autowired
	private StudyProgramService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<StudyProgramDto>> getAll(){
		List<StudyProgramDto> studyPrograms = new ArrayList<StudyProgramDto>();
		for (StudyProgram sp : service.findAll()) {
			if(sp.getFaculty()!=null) {
				Set<String> courseCodes = sp.getCourses()
	                    .stream()
	                    .map(course -> course.getCourseCode())
	                    .collect(Collectors.toSet());
				StudyProgramDto programDTO = new StudyProgramDto(sp.getId(), sp.getProgramCode(), sp.getDescription(), sp.getName(),  sp.getProgramDirector(),  new FacultyDto(sp.getFaculty().getId(),sp.getFaculty().getFacultyCode(),sp.getFaculty().getName()), courseCodes);
	
				studyPrograms.add(programDTO);
			}else {
				Set<String> courseCodes = sp.getCourses()
	                    .stream()
	                    .map(course -> course.getCourseCode())
	                    .collect(Collectors.toSet());
				StudyProgramDto programDTO = new StudyProgramDto(sp.getId(), sp.getProgramCode(), sp.getDescription(), sp.getName(),  sp.getProgramDirector(), null, courseCodes);
	
				studyPrograms.add(programDTO);
		}
			}
		
		return new ResponseEntity<Iterable<StudyProgramDto>>(studyPrograms, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<StudyProgramDto> get(@PathVariable("id") Long id){
		Optional<StudyProgram> sp = service.findOne(id);
		if(sp.isPresent() && sp.get().getFaculty()!= null) {
			Set<String> courseCodes = sp.get().getCourses()
                    .stream()
                    .map(course -> course.getCourseCode())
                    .collect(Collectors.toSet());
			StudyProgramDto dto = new StudyProgramDto(sp.get().getId(),sp.get().getProgramCode(), sp.get().getDescription(), sp.get().getName(), sp.get().getProgramDirector(), new FacultyDto(sp.get().getFaculty().getId(),sp.get().getFaculty().getFacultyCode(),sp.get().getFaculty().getName()), courseCodes);
			return new ResponseEntity<StudyProgramDto>(dto, HttpStatus.OK);
		}else if(sp.isPresent()) {
			Set<String> courseCodes = sp.get().getCourses()
                    .stream()
                    .map(course -> course.getCourseCode())
                    .collect(Collectors.toSet());
			StudyProgramDto dto = new StudyProgramDto(sp.get().getId(),sp.get().getProgramCode(), sp.get().getDescription(), sp.get().getName(), sp.get().getProgramDirector(), null, courseCodes);
			return new ResponseEntity<StudyProgramDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<StudyProgramDto>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = "/s/{programCode}", method = RequestMethod.GET)
	public ResponseEntity<StudyProgramDto> get(@PathVariable("programCode") String programCode){
		Optional<StudyProgram> sp = service.findByProgramCode(programCode);
		if(sp.isPresent() && sp.get().getFaculty()!=null) {
			Set<String> courseCodes = sp.get().getCourses()
                    .stream()
                    .map(course -> course.getCourseCode())
                    .collect(Collectors.toSet());
			StudyProgramDto dto = new StudyProgramDto(sp.get().getId(),sp.get().getProgramCode(), sp.get().getDescription(), sp.get().getName(), sp.get().getProgramDirector(), new FacultyDto(sp.get().getFaculty().getId(),sp.get().getFaculty().getFacultyCode(),sp.get().getFaculty().getName()), courseCodes);
			return new ResponseEntity<StudyProgramDto>(dto, HttpStatus.OK);
		}else if(sp.isPresent()) {
			Set<String> courseCodes = sp.get().getCourses()
                    .stream()
                    .map(course -> course.getCourseCode())
                    .collect(Collectors.toSet());
			StudyProgramDto dto = new StudyProgramDto(sp.get().getId(),sp.get().getProgramCode(), sp.get().getDescription(), sp.get().getName(), sp.get().getProgramDirector(),null , courseCodes);
			return new ResponseEntity<StudyProgramDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<StudyProgramDto>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<StudyProgram> create(@RequestBody StudyProgram r){
		try {
			service.save(r);
			return new ResponseEntity<StudyProgram>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<StudyProgram>(HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<StudyProgram> update(@PathVariable("id") Long id, @RequestBody StudyProgram sp){
		StudyProgram u = service.findOne(id).orElse(null);
		if(u != null) {
			sp.setId(id);
			sp = service.save(sp);
			return new ResponseEntity<StudyProgram>(HttpStatus.OK);
		}
		return new ResponseEntity<StudyProgram>(HttpStatus.NOT_FOUND);
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

