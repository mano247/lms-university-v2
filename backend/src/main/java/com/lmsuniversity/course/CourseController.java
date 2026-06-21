package com.lmsuniversity.course;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lmsuniversity.security.services.UserDetailsImpl;

@Controller
@RequestMapping(path = "/api/courses")
public class CourseController {
	@Autowired
	private CourseService service;

	@Autowired
	private CourseMapper mapper;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<CourseDto>> getAll() {
		List<CourseDto> courses = mapper.toDtoList(service.findAll());
		return new ResponseEntity<List<CourseDto>>(courses, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CourseDto> getById(@PathVariable("id") Long id){
		Optional<Course> p = service.findOne(id);
		if(p.isPresent()) {
			return new ResponseEntity<CourseDto>(mapper.toDto(p.get()), HttpStatus.OK);
		}
		return new ResponseEntity<CourseDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/code/{courseCode}", method = RequestMethod.GET)
	public ResponseEntity<CourseDto> getCourseCode(@PathVariable("courseCode") String courseCode){
		Optional<Course> p = service.findByCourseCode(courseCode);
		if(p.isPresent()) {
			return new ResponseEntity<CourseDto>(mapper.toDto(p.get()), HttpStatus.OK);
		}
		return new ResponseEntity<CourseDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<CourseDto> create(@Valid @RequestBody CourseCreateDto dto){
		Course course = service.create(dto);
		return new ResponseEntity<CourseDto>(mapper.toDto(course), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<CourseDto> update(@PathVariable("id") Long id, @Valid @RequestBody CourseUpdateDto dto){
		Course course = service.update(id, dto);
		if(course != null) {
			return new ResponseEntity<CourseDto>(mapper.toDto(course), HttpStatus.OK);
		}
		return new ResponseEntity<CourseDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Course> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<Course>(HttpStatus.OK);
		}
		return new ResponseEntity<Course>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}/assign-teacher", method = RequestMethod.POST)
	public ResponseEntity<CourseDto> assignTeacherToCourse(@PathVariable("id") Long id, @Valid @RequestBody AssignTeacherDto dto){
		Course course = service.assignTeacher(id, dto.getTeacherId());
		if(course != null) {
			return new ResponseEntity<CourseDto>(mapper.toDto(course), HttpStatus.OK);
		}
		return new ResponseEntity<CourseDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "/{id}/syllabus", method = RequestMethod.PUT)
	public ResponseEntity<CourseDto> updateCourseSyllabus(@PathVariable("id") Long id, @RequestBody UpdateSyllabusDto dto, Authentication authentication){
		if(authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();
			Course course = service.updateSyllabus(id, dto.getSyllabus(), userId);
			if(course != null) {
				return new ResponseEntity<CourseDto>(mapper.toDto(course), HttpStatus.OK);
			}
		}
		return new ResponseEntity<CourseDto>(HttpStatus.NOT_FOUND);
	}

}
