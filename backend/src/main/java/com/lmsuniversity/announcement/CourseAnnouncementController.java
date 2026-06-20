package com.lmsuniversity.announcement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import com.lmsuniversity.user.TeacherDto;
import com.lmsuniversity.course.CourseDto;
import com.lmsuniversity.studyprogram.StudyProgramDto;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.security.services.UserDetailsImpl;
import com.lmsuniversity.course.CourseService;

@Controller
@RequestMapping(path = "/api/predmetnaObavestenja")
@CrossOrigin(origins = "http://localhost:4200")
public class CourseAnnouncementController {
	@Autowired
	private CourseAnnouncementService service;

	@Autowired
	private CourseService courseService;


	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<CourseAnnouncementDto>> getAll(Authentication authentication){
		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			ArrayList<CourseAnnouncementDto> courseAnnouncements = new ArrayList<CourseAnnouncementDto>();
			for (CourseAnnouncement o : service.findAll()) {

				CourseDto course = new CourseDto(o.getCourse().getId(),o.getCourse().getCourseCode(), o.getCourse().getSyllabus(), o.getCourse().getName(), o.getCourse().getEcts(),
						new TeacherDto(o.getCourse().getTeacher().getId(),o.getCourse().getTeacher().getFirstName(),o.getCourse().getTeacher().getLastName()), o.getCourse().getStartDate(), o.getCourse().getEndDate(),
						o.getCourse().getDescription(), o.getCourse().getTeachingMaterials().stream().map(
								nm -> new TeachingMaterialDto(nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPublicationYear(), nm.getPublisher(), nm.getDescription(), nm.getUrl(), nm.getOutcome(), nm.getQuantity(), nm.getIssuedQuantity()
								)).collect(Collectors.toSet()));

				Optional<Course> pr = courseService.findOne(o.getCourse().getId());
				List<Long> id = new ArrayList<>();
				for(Student s: pr.get().getStudents()) {
					id.add(s.getId());
				}

				if (id.contains(userId)) {

					courseAnnouncements.add(new CourseAnnouncementDto(o.getId(), o.getTitle(), o.getContent(), o.getDate(), o.getImage(), course, o.getStartDate(), o.getEndDate()));
				}else if(pr.get().getTeacher().getId() == userId) {
					courseAnnouncements.add(new CourseAnnouncementDto(o.getId(), o.getTitle(), o.getContent(), o.getDate(), o.getImage(), course, o.getStartDate(), o.getEndDate()));

				}

			}
			return new ResponseEntity<Iterable<CourseAnnouncementDto>>(courseAnnouncements, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CourseAnnouncementDto> get(@PathVariable("id") Long id){
		Optional<CourseAnnouncement> o = service.findOne(id);
		if(o.isPresent()) {
			CourseDto course = new CourseDto(
					o.get().getCourse().getId(),
					o.get().getCourse().getCourseCode(),
					new StudyProgramDto(o.get().getCourse().getStudyProgram().getId(),o.get().getCourse().getStudyProgram().getProgramCode(),o.get().getCourse().getStudyProgram().getName()),
					o.get().getCourse().getSyllabus(),
					o.get().getCourse().getName(),
					o.get().getCourse().getEcts(),
					new TeacherDto(o.get().getCourse().getTeacher().getId(),o.get().getCourse().getTeacher().getFirstName(),o.get().getCourse().getTeacher().getLastName()),
					o.get().getCourse().getStartDate(),
					o.get().getCourse().getEndDate(),
					o.get().getCourse().getDescription(),
					o.get().getCourse().getTeachingMaterials().stream().map(nm -> new TeachingMaterialDto(nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPublicationYear(), nm.getPublisher(), nm.getDescription(), nm.getUrl(), nm.getOutcome(), nm.getQuantity(), nm.getIssuedQuantity()
									)).collect(Collectors.toSet()));

			CourseAnnouncementDto announcement = new CourseAnnouncementDto(o.get().getId(), o.get().getTitle(), o.get().getContent(), o.get().getDate(), o.get().getImage(), course, o.get().getStartDate(), o.get().getEndDate());
			return new ResponseEntity<CourseAnnouncementDto>(announcement, HttpStatus.OK);
		}
		return new ResponseEntity<CourseAnnouncementDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<CourseAnnouncement> create(@RequestBody CourseAnnouncement r, Authentication authentication){
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				Optional<Course> p = courseService.findOne(r.getCourse().getId());

				if(p.get().getTeacher().getId().equals(userId)) {
					r.setImage("");
					r.setDate(LocalDateTime.now());
					service.save(r);
					return new ResponseEntity<CourseAnnouncement>(r, HttpStatus.CREATED);
				}
			}
		return new ResponseEntity<CourseAnnouncement>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<CourseAnnouncement> update(@PathVariable("id") Long id, @RequestBody CourseAnnouncement courseAnnouncement, Authentication authentication){

		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			Optional<Course> p = courseService.findOne(service.findOne(id).get().getCourse().getId());

			if (p.get().getTeacher().getId().equals(userId)) {
				CourseAnnouncement announcementToUpdate = service.findOne(id).orElse(null);
				if(announcementToUpdate != null) {
					announcementToUpdate.setId(id);
					announcementToUpdate.setTitle(courseAnnouncement.getTitle());
					announcementToUpdate.setContent(courseAnnouncement.getContent());
					announcementToUpdate.setDate(LocalDateTime.now());
					announcementToUpdate.setImage("");
					announcementToUpdate.setCourse(announcementToUpdate.getCourse());
					@SuppressWarnings("unused")
					CourseAnnouncement updatedAnnouncement = service.save(announcementToUpdate);
					return new ResponseEntity<CourseAnnouncement>(HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<CourseAnnouncement>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<CourseAnnouncement> delete(@PathVariable("id") Long id, Authentication authentication) {

		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			Optional<Course> p = courseService.findOne(service.findOne(id).get().getCourse().getId());

			if(p.get().getTeacher().getId().equals(userId)) {

				if (service.findOne(id).isPresent()) {
					service.delete(id);
					return new ResponseEntity<CourseAnnouncement>(HttpStatus.OK);
				}
			}


		}
		return new ResponseEntity<CourseAnnouncement>(HttpStatus.NOT_FOUND);
	}

@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
@RequestMapping(path = "gbp/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<CourseAnnouncementDto>> getByCourse(@PathVariable("id") Long id, Authentication authentication){


		ArrayList<CourseAnnouncementDto> courseAnnouncements = new ArrayList<CourseAnnouncementDto>();
		for (CourseAnnouncement o : service.findAll()) {

			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();
				if (o.getCourse().getId().equals(id)) {
					CourseDto course = new CourseDto(
							o.getCourse().getId(),
							o.getCourse().getCourseCode(),
							o.getCourse().getSyllabus(),
							o.getCourse().getName(),
							o.getCourse().getEcts(),
							new TeacherDto(o.getCourse().getTeacher().getId(),o.getCourse().getTeacher().getFirstName(),o.getCourse().getTeacher().getLastName()), o.getCourse().getStartDate(), o.getCourse().getEndDate(),
							o.getCourse().getDescription(),
							o.getCourse().getTeachingMaterials()
							.stream()
							.map( nm -> new TeachingMaterialDto(nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPublicationYear(), nm.getPublisher(), nm.getDescription(), nm.getUrl(), nm.getOutcome(), nm.getQuantity(), nm.getIssuedQuantity()
								)).collect(Collectors.toSet()));

					Optional<Course> pr = courseService.findOne(o.getCourse().getId());
					List<Long> idlist = new ArrayList<>();
					for(Student s: pr.get().getStudents()) {
						idlist.add(s.getId());
					}

					if (idlist.contains(userId)) {
						courseAnnouncements.add(new CourseAnnouncementDto(o.getId(), o.getTitle(), o.getContent(), o.getDate(), o.getImage(), course, o.getStartDate(), o.getEndDate()));
					}else if(pr.get().getTeacher().getId() == userId) {
						courseAnnouncements.add(new CourseAnnouncementDto(o.getId(), o.getTitle(), o.getContent(), o.getDate(), o.getImage(), course, o.getStartDate(), o.getEndDate()));
					}
		}
				}
		return new ResponseEntity<Iterable<CourseAnnouncementDto>>(courseAnnouncements, HttpStatus.OK);
}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
}
