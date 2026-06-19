package rs.ac.singidunum.novisad.backend.controller;

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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rs.ac.singidunum.novisad.backend.dto.TeachingMaterialDTO;
import rs.ac.singidunum.novisad.backend.dto.TeacherDTO;
import rs.ac.singidunum.novisad.backend.dto.CourseDTO;
import rs.ac.singidunum.novisad.backend.dto.StudyProgramDTO;
import rs.ac.singidunum.novisad.backend.model.academic.Course;
import rs.ac.singidunum.novisad.backend.security.services.UserDetailsImpl;
import rs.ac.singidunum.novisad.backend.service.PredmetService;

@Controller
@RequestMapping(path = "/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
public class PredmetController {
	@Autowired
	private PredmetService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<CourseDTO>> getAll() {
		ArrayList<CourseDTO> courses = new ArrayList<CourseDTO>();
		for (Course p : service.findAll()) {
			Set<TeachingMaterialDTO> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> new TeachingMaterialDTO(
					nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
					nm.getQuantity(),
					nm.getIssuedQuantity()
			)).collect(Collectors.toSet());
			courses.add(new CourseDTO(p.getId(),p.getCourseCode(), 
					new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()), 
					p.getSyllabus(),p.getName(),p.getEcts(), 
						new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName())
						, p.getStartDate(),p.getEndDate(), p.getDescription(),teachingMaterials));
		}
		return new ResponseEntity<Iterable<CourseDTO>>(courses, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CourseDTO> getById(@PathVariable("id") Long id){
		Optional<Course> p = service.findOne(id);
		if(p.isPresent()) {
			Set<TeachingMaterialDTO> teachingMaterials = p.get().getTeachingMaterials().stream().map(nm -> new TeachingMaterialDTO(
					nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
					nm.getQuantity(),
					nm.getIssuedQuantity()
			)).collect(Collectors.toSet());
			try {
				CourseDTO dto = new CourseDTO(p.get().getId(),p.get().getCourseCode(),
						new StudyProgramDTO(p.get().getStudyProgram().getId(),p.get().getStudyProgram().getProgramCode(),p.get().getStudyProgram().getName()),p.get().getSyllabus(), p.get().getName(),p.get().getEcts(), new TeacherDTO(p.get().getTeacher().getId(),p.get().getTeacher().getFirstName(),p.get().getTeacher().getLastName()),p.get().getStartDate(),p.get().getEndDate() , p.get().getDescription(), teachingMaterials);
				return new ResponseEntity<CourseDTO>(dto, HttpStatus.OK);
			} catch (Exception e) {
				CourseDTO dto = new CourseDTO(p.get().getId(),p.get().getCourseCode(),
						new StudyProgramDTO(p.get().getStudyProgram().getId(),p.get().getStudyProgram().getProgramCode(),p.get().getStudyProgram().getName()),p.get().getSyllabus(), p.get().getName(),p.get().getEcts(),null,p.get().getStartDate(),p.get().getEndDate() , p.get().getDescription(), teachingMaterials);
				return new ResponseEntity<CourseDTO>(dto, HttpStatus.OK);
			}
		}
		return new ResponseEntity<CourseDTO>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/s/{courseCode}", method = RequestMethod.GET)
	public ResponseEntity<CourseDTO> getCourseCode(@PathVariable("courseCode") String courseCode){
		Optional<Course> p = service.findBysifraPredmeta(courseCode);
		if(p.isPresent()) {
			Set<TeachingMaterialDTO> teachingMaterials = p.get().getTeachingMaterials().stream().map(nm -> new TeachingMaterialDTO(
					nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
					nm.getQuantity(),
					nm.getIssuedQuantity(),
					nm.getPublicationYear()
			)).collect(Collectors.toSet());
			
			CourseDTO dto = new CourseDTO(
					p.get().getId(),
					p.get().getCourseCode(), 
					new StudyProgramDTO(p.get().getStudyProgram().getId(),p.get().getStudyProgram().getProgramCode(),p.get().getStudyProgram().getName()),
					p.get().getSyllabus(), 					
					p.get().getName(),
					p.get().getEcts(),
					new TeacherDTO(p.get().getTeacher().getId(),p.get().getTeacher().getFirstName(),p.get().getTeacher().getLastName()) ,
					p.get().getStartDate(),
					p.get().getEndDate(),
					p.get().getDescription(),
					teachingMaterials);
			return new ResponseEntity<CourseDTO>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<CourseDTO>(HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Course> create(@RequestBody Course p){
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
	public ResponseEntity<Course> update(@PathVariable("id") Long id, @RequestBody Course course){
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
	@RequestMapping(path = "/{id}/dodajNastavnika", method = RequestMethod.POST)
	public ResponseEntity<Course> dodeliNastavnikaPredmetu(@PathVariable("id") Long id, @RequestBody Course course){
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
	@RequestMapping(path = "/{id}/izmeniSilabus", method = RequestMethod.PUT)
	public ResponseEntity<Course> izmenaSilabusaPredmeta(@PathVariable("id") Long id, @RequestBody Course course, Authentication authentication){
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
