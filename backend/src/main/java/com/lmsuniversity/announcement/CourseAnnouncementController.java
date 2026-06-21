package com.lmsuniversity.announcement;

import java.util.ArrayList;
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

import com.lmsuniversity.course.Course;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.security.services.UserDetailsImpl;
import com.lmsuniversity.course.CourseService;

@Controller
@RequestMapping(path = "/api/course-announcements")
public class CourseAnnouncementController {
	@Autowired
	private CourseAnnouncementService service;

	@Autowired
	private CourseAnnouncementMapper mapper;

	@Autowired
	private CourseService courseService;


	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<CourseAnnouncementDto>> getAll(Authentication authentication){
		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			List<CourseAnnouncementDto> courseAnnouncements = new ArrayList<CourseAnnouncementDto>();
			for (CourseAnnouncement o : service.findAll()) {

				Optional<Course> pr = courseService.findOne(o.getCourse().getId());
				List<Long> id = new ArrayList<>();
				for(Student s: pr.get().getStudents()) {
					id.add(s.getId());
				}

				if (id.contains(userId) || pr.get().getTeacher().getId().equals(userId)) {
					courseAnnouncements.add(mapper.toDto(o));
				}

			}
			return new ResponseEntity<List<CourseAnnouncementDto>>(courseAnnouncements, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CourseAnnouncementDto> get(@PathVariable("id") Long id, Authentication authentication){
		Optional<CourseAnnouncement> o = service.findOne(id);
		if(o.isPresent()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			Course course = o.get().getCourse();
			boolean isEnrolled = course.getStudents().stream().anyMatch(s -> s.getId().equals(userId));
			boolean isOwningTeacher = course.getTeacher().getId().equals(userId);

			if (isEnrolled || isOwningTeacher) {
				return new ResponseEntity<CourseAnnouncementDto>(mapper.toDto(o.get()), HttpStatus.OK);
			}
			return new ResponseEntity<CourseAnnouncementDto>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<CourseAnnouncementDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<CourseAnnouncementDto> create(@Valid @RequestBody CourseAnnouncementCreateDto dto, Authentication authentication){
		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			CourseAnnouncement courseAnnouncement = service.create(dto, userId);
			if (courseAnnouncement != null) {
				return new ResponseEntity<CourseAnnouncementDto>(mapper.toDto(courseAnnouncement), HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<CourseAnnouncementDto>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<CourseAnnouncementDto> update(@PathVariable("id") Long id, @Valid @RequestBody CourseAnnouncementUpdateDto dto, Authentication authentication){

		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();
			boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
			boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");

			CourseAnnouncement courseAnnouncement = service.update(id, dto, userId, isAdmin, isStudentAffairs);
			if (courseAnnouncement != null) {
				return new ResponseEntity<CourseAnnouncementDto>(mapper.toDto(courseAnnouncement), HttpStatus.OK);
			}
		}
		return new ResponseEntity<CourseAnnouncementDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<CourseAnnouncement> delete(@PathVariable("id") Long id, Authentication authentication) {

		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();
			boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
			boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");

			if (service.delete(id, userId, isAdmin, isStudentAffairs)) {
				return new ResponseEntity<CourseAnnouncement>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<CourseAnnouncement>(HttpStatus.NOT_FOUND);
	}

	private boolean hasAuthority(Authentication authentication, String authority) {
		return authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals(authority));
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/by-course/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<CourseAnnouncementDto>> getByCourse(@PathVariable("id") Long id, Authentication authentication){

		List<CourseAnnouncementDto> courseAnnouncements = new ArrayList<CourseAnnouncementDto>();
		for (CourseAnnouncement o : service.findAll()) {

			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();
				if (o.getCourse().getId().equals(id)) {

					Optional<Course> pr = courseService.findOne(o.getCourse().getId());
					List<Long> idlist = new ArrayList<>();
					for(Student s: pr.get().getStudents()) {
						idlist.add(s.getId());
					}

					if (idlist.contains(userId) || pr.get().getTeacher().getId().equals(userId)) {
						courseAnnouncements.add(mapper.toDto(o));
					}
		}
				}
		}
		return new ResponseEntity<List<CourseAnnouncementDto>>(courseAnnouncements, HttpStatus.OK);
	}
}
