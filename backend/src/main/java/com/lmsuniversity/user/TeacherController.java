package com.lmsuniversity.user;

import java.util.Set;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lmsuniversity.teachingmaterial.TeachingMaterialDto;
import com.lmsuniversity.common.TeacherCourseDto;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.security.services.UserDetailsImpl;

@Controller
@RequestMapping(path = "/api/teachers")
@CrossOrigin(origins = "http://localhost:4200")
public class TeacherController {
	@Autowired
	private TeacherService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<TeacherDto>> getAll(){
		ArrayList<TeacherDto> teachers = new ArrayList<TeacherDto>();
		for (Teacher p : service.findAll()) {
			teachers.add(new TeacherDto(p.getClass().getSimpleName(), p.getId(), p.getUsername(),  p.getEmail(), p.getPassword(),p.getFirstName(), p.getLastName()));
		}
		return new ResponseEntity<Iterable<TeacherDto>>(teachers, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TeacherDto> get(@PathVariable("id") Long id){
		Optional<Teacher> p = service.findOne(id);
		if(p.isPresent()) {
			TeacherDto teacher = new TeacherDto(p.get().getClass().getSimpleName(), p.get().getId() ,p.get().getUsername(), p.get().getEmail(), p.get().getPassword(), p.get().getFirstName(), p.get().getLastName());
			return new ResponseEntity<TeacherDto>(teacher, HttpStatus.OK);
		}
		return new ResponseEntity<TeacherDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Teacher> create(@RequestBody Teacher r){
		try {
			service.save(r);
			return new ResponseEntity<Teacher>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Teacher>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Teacher> update(@PathVariable("id") Long id, @RequestBody Teacher teacher, Authentication authentication){
		if(authentication.isAuthenticated()) {

				Teacher u = service.findOne(id).orElse(null);
				if(u != null && u.getUniversity()!= null) {
					teacher.setId(id);
					teacher.setUniversity(u.getUniversity());
					teacher.setPassword(u.getPassword());
					teacher.setPermissions(u.getPermissions());
					teacher = service.save(teacher);
					return new ResponseEntity<Teacher>(HttpStatus.OK);
				}else if (u != null) {
					teacher.setId(id);
					teacher.setPassword(u.getPassword());
					teacher.setPermissions(u.getPermissions());
					teacher = service.save(teacher);
					return new ResponseEntity<Teacher>(HttpStatus.OK);
				}

		}
		return  new ResponseEntity<Teacher>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Teacher> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<Teacher>(HttpStatus.OK);
		}
		return new ResponseEntity<Teacher>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "/{id}/mojiPredmeti", method = RequestMethod.GET)
	public ResponseEntity<Set<TeacherCourseDto>> getMyCourses(@PathVariable("id") Long id, Authentication authentication){
		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			if(id.equals(userId)) {
				Optional<Teacher> p = service.findOne(id);
				Set<Course> courses = p.get().getCourses();

				if(p.isPresent()) {
					Set<TeacherCourseDto> teacherCourses = courses.stream().map(pr -> new TeacherCourseDto(
							pr.getId(),
							pr.getCourseCode(),
							pr.getSyllabus(),
							pr.getName(),
							pr.getEcts(),
							new TeacherDto(pr.getTeacher().getId(),pr.getTeacher().getFirstName(),pr.getTeacher().getLastName()),
							pr.getStartDate(),
							pr.getEndDate(),
							pr.getDescription(),
							pr.getStudyProgram().getName(),
							pr.getTeachingMaterials().stream().map(nm -> new TeachingMaterialDto(nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),nm.getQuantity(),nm.getIssuedQuantity())).collect(Collectors.toSet()),
							pr.getStudents().stream().map(st -> new StudentDto(st.getId(),st.getEmail(),st.getUsername(),st.getIndexNumber(),st.getFirstName(),st.getLastName(),st.getFaculty())).collect(Collectors.toSet())
					)).collect(Collectors.toSet());


					return new ResponseEntity<Set<TeacherCourseDto>>(teacherCourses, HttpStatus.OK);
				}
			}

		}
		return new ResponseEntity<Set<TeacherCourseDto>>(HttpStatus.NOT_FOUND);
	}
}
