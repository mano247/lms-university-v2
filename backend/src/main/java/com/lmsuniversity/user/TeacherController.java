package com.lmsuniversity.user;

import java.util.Set;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.lmsuniversity.common.TeacherCourseDto;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.security.services.UserDetailsImpl;

@Controller
@RequestMapping(path = "/api/teachers")
public class TeacherController {
	@Autowired
	private TeacherService service;

	@Autowired
	private TeacherMapper mapper;

	@Autowired
	private StudentMapper studentMapper;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Page<TeacherDto>> getAll(Pageable pageable){
		Page<TeacherDto> teachers = service.findAll(pageable).map(mapper::toDto);
		return new ResponseEntity<Page<TeacherDto>>(teachers, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TeacherDto> get(@PathVariable("id") Long id){
		Optional<Teacher> p = service.findOne(id);
		if(p.isPresent()) {
			return new ResponseEntity<TeacherDto>(mapper.toDto(p.get()), HttpStatus.OK);
		}
		return new ResponseEntity<TeacherDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<TeacherDto> create(@Valid @RequestBody TeacherCreateDto dto){
		Teacher teacher = service.create(dto);
		return new ResponseEntity<TeacherDto>(mapper.toDto(teacher), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<TeacherDto> update(@PathVariable("id") Long id, @Valid @RequestBody TeacherUpdateDto dto, Authentication authentication){
		if(authentication.isAuthenticated()) {
			Teacher teacher = service.update(id, dto);
			if(teacher != null) {
				return new ResponseEntity<TeacherDto>(mapper.toDto(teacher), HttpStatus.OK);
			}
		}
		return new ResponseEntity<TeacherDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Teacher> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<Teacher>(HttpStatus.OK);
		}
		return new ResponseEntity<Teacher>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "/{id}/my-courses", method = RequestMethod.GET)
	public ResponseEntity<Set<TeacherCourseDto>> getMyCourses(@PathVariable("id") Long id, Authentication authentication){
		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			if(id.equals(userId)) {
				Optional<Teacher> p = service.findOne(id);

				if(p.isPresent()) {
					Set<Course> courses = p.get().getCourses();
					Set<TeacherCourseDto> teacherCourses = courses.stream().map(pr -> TeacherCourseDto.builder()
							.id(pr.getId())
							.courseCode(pr.getCourseCode())
							.syllabus(pr.getSyllabus())
							.name(pr.getName())
							.ects(pr.getEcts())
							.teacher(TeacherDto.builder().id(pr.getTeacher().getId()).firstName(pr.getTeacher().getFirstName()).lastName(pr.getTeacher().getLastName()).build())
							.startDate(pr.getStartDate())
							.endDate(pr.getEndDate())
							.description(pr.getDescription())
							.studyProgram(pr.getStudyProgram().getName())
							.teachingMaterials(pr.getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet()))
							.students(pr.getStudents().stream().map(studentMapper::toDto).collect(Collectors.toSet()))
							.build()
					).collect(Collectors.toSet());


					return new ResponseEntity<Set<TeacherCourseDto>>(teacherCourses, HttpStatus.OK);
				}
			}

		}
		return new ResponseEntity<Set<TeacherCourseDto>>(HttpStatus.NOT_FOUND);
	}
}
