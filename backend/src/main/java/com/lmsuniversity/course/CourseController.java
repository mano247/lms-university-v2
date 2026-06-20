package com.lmsuniversity.course;

import java.util.ArrayList;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.lmsuniversity.teachingmaterial.TeachingMaterialDto;
import com.lmsuniversity.user.TeacherDto;
import com.lmsuniversity.studyprogram.StudyProgramDto;
import com.lmsuniversity.security.services.UserDetailsImpl;

@Controller
@RequestMapping(path = "/api/courses")
public class CourseController {
	@Autowired
	private CourseService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<CourseDto>> getAll() {
		ArrayList<CourseDto> courses = new ArrayList<CourseDto>();
		for (Course p : service.findAll()) {
			Set<TeachingMaterialDto> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet());
			courses.add(CourseDto.builder()
					.id(p.getId())
					.courseCode(p.getCourseCode())
					.studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build())
					.syllabus(p.getSyllabus())
					.name(p.getName())
					.ects(p.getEcts())
					.teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build())
					.startDate(p.getStartDate())
					.endDate(p.getEndDate())
					.description(p.getDescription())
					.teachingMaterials(teachingMaterials)
					.build());
		}
		return new ResponseEntity<Iterable<CourseDto>>(courses, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CourseDto> getById(@PathVariable("id") Long id){
		Optional<Course> p = service.findOne(id);
		if(p.isPresent()) {
			Set<TeachingMaterialDto> teachingMaterials = p.get().getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet());
			try {
				CourseDto dto = CourseDto.builder()
						.id(p.get().getId())
						.courseCode(p.get().getCourseCode())
						.studyProgram(StudyProgramDto.builder().id(p.get().getStudyProgram().getId()).programCode(p.get().getStudyProgram().getProgramCode()).name(p.get().getStudyProgram().getName()).build())
						.syllabus(p.get().getSyllabus())
						.name(p.get().getName())
						.ects(p.get().getEcts())
						.teacher(TeacherDto.builder().id(p.get().getTeacher().getId()).firstName(p.get().getTeacher().getFirstName()).lastName(p.get().getTeacher().getLastName()).build())
						.startDate(p.get().getStartDate())
						.endDate(p.get().getEndDate())
						.description(p.get().getDescription())
						.teachingMaterials(teachingMaterials)
						.build();
				return new ResponseEntity<CourseDto>(dto, HttpStatus.OK);
			} catch (Exception e) {
				CourseDto dto = CourseDto.builder()
						.id(p.get().getId())
						.courseCode(p.get().getCourseCode())
						.studyProgram(StudyProgramDto.builder().id(p.get().getStudyProgram().getId()).programCode(p.get().getStudyProgram().getProgramCode()).name(p.get().getStudyProgram().getName()).build())
						.syllabus(p.get().getSyllabus())
						.name(p.get().getName())
						.ects(p.get().getEcts())
						.teacher(null)
						.startDate(p.get().getStartDate())
						.endDate(p.get().getEndDate())
						.description(p.get().getDescription())
						.teachingMaterials(teachingMaterials)
						.build();
				return new ResponseEntity<CourseDto>(dto, HttpStatus.OK);
			}
		}
		return new ResponseEntity<CourseDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/code/{courseCode}", method = RequestMethod.GET)
	public ResponseEntity<CourseDto> getCourseCode(@PathVariable("courseCode") String courseCode){
		Optional<Course> p = service.findByCourseCode(courseCode);
		if(p.isPresent()) {
			Set<TeachingMaterialDto> teachingMaterials = p.get().getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).publicationYear(nm.getPublicationYear()).build()).collect(Collectors.toSet());
			
			CourseDto dto = CourseDto.builder()
					.id(p.get().getId())
					.courseCode(p.get().getCourseCode())
					.studyProgram(StudyProgramDto.builder().id(p.get().getStudyProgram().getId()).programCode(p.get().getStudyProgram().getProgramCode()).name(p.get().getStudyProgram().getName()).build())
					.syllabus(p.get().getSyllabus())
					.name(p.get().getName())
					.ects(p.get().getEcts())
					.teacher(TeacherDto.builder().id(p.get().getTeacher().getId()).firstName(p.get().getTeacher().getFirstName()).lastName(p.get().getTeacher().getLastName()).build())
					.startDate(p.get().getStartDate())
					.endDate(p.get().getEndDate())
					.description(p.get().getDescription())
					.teachingMaterials(teachingMaterials)
					.build();
			return new ResponseEntity<CourseDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<CourseDto>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Course> create(@Valid @RequestBody Course p){
		try {
			service.save(p);
			return new ResponseEntity<Course>(p, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Course>(HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Course> update(@PathVariable("id") Long id, @Valid @RequestBody Course course){
		Course p = service.findOne(id).orElse(null);
		if(p != null) {
			course.setId(id);
			course = service.save(course);
			return new ResponseEntity<Course>(HttpStatus.OK);
		}
		return new ResponseEntity<Course>(HttpStatus.NOT_FOUND);
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
	public ResponseEntity<Course> assignTeacherToCourse(@PathVariable("id") Long id, @RequestBody Course course){
		Course p = service.findOne(id).orElse(null);
		if(p != null) {
			course.setId(id);
			p.setTeacher(course.getTeacher());
			p = service.save(p);
			return new ResponseEntity<Course>(HttpStatus.OK);
		}
		return new ResponseEntity<Course>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "/{id}/syllabus", method = RequestMethod.PUT)
	public ResponseEntity<Course> updateCourseSyllabus(@PathVariable("id") Long id, @RequestBody Course course, Authentication authentication){
		if(authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();
			Course p = service.findOne(id).orElse(null);
			if(p.getTeacher().getId().equals(userId)) {
				
				if(p != null) {
					course.setId(id);
					course.setCourseCode(p.getCourseCode());
					course.setName(p.getName());
					course.setEcts(p.getEcts());
					course.setDescription(p.getDescription());
					course.setSyllabus(course.getSyllabus());
					course.setTeachingMaterials(p.getTeachingMaterials());
					course.setStudyProgram(p.getStudyProgram());
					course.setTeacher(p.getTeacher());
					course.setStudents(p.getStudents());
					course.setExamAttempts(p.getExamAttempts());
					course.setAnnouncements(p.getAnnouncements());
					course.setEndDate(p.getEndDate());
					course.setStartDate(p.getStartDate());
					course = service.save(course);
					return new ResponseEntity<Course>(HttpStatus.OK);
				}
			}
		
		}
		return new ResponseEntity<Course>(HttpStatus.NOT_FOUND);
	}
	

}
