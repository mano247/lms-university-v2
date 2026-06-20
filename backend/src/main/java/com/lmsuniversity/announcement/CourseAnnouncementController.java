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
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
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
@RequestMapping(path = "/api/course-announcements")
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

				CourseDto course = CourseDto.builder()
						.id(o.getCourse().getId())
						.courseCode(o.getCourse().getCourseCode())
						.syllabus(o.getCourse().getSyllabus())
						.name(o.getCourse().getName())
						.ects(o.getCourse().getEcts())
						.teacher(TeacherDto.builder().id(o.getCourse().getTeacher().getId()).firstName(o.getCourse().getTeacher().getFirstName()).lastName(o.getCourse().getTeacher().getLastName()).build())
						.startDate(o.getCourse().getStartDate())
						.endDate(o.getCourse().getEndDate())
						.description(o.getCourse().getDescription())
						.teachingMaterials(o.getCourse().getTeachingMaterials().stream().map(
								nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).publicationYear(nm.getPublicationYear()).publisher(nm.getPublisher()).description(nm.getDescription()).url(nm.getUrl()).outcome(nm.getOutcome()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet()))
						.build();

				Optional<Course> pr = courseService.findOne(o.getCourse().getId());
				List<Long> id = new ArrayList<>();
				for(Student s: pr.get().getStudents()) {
					id.add(s.getId());
				}

				if (id.contains(userId)) {

					courseAnnouncements.add(CourseAnnouncementDto.builder().id(o.getId()).title(o.getTitle()).content(o.getContent()).date(o.getDate()).imageUrl(o.getImage()).course(course).startDate(o.getStartDate()).endDate(o.getEndDate()).build());
				}else if(pr.get().getTeacher().getId() == userId) {
					courseAnnouncements.add(CourseAnnouncementDto.builder().id(o.getId()).title(o.getTitle()).content(o.getContent()).date(o.getDate()).imageUrl(o.getImage()).course(course).startDate(o.getStartDate()).endDate(o.getEndDate()).build());

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
			CourseDto course = CourseDto.builder()
					.id(o.get().getCourse().getId())
					.courseCode(o.get().getCourse().getCourseCode())
					.studyProgram(StudyProgramDto.builder().id(o.get().getCourse().getStudyProgram().getId()).programCode(o.get().getCourse().getStudyProgram().getProgramCode()).name(o.get().getCourse().getStudyProgram().getName()).build())
					.syllabus(o.get().getCourse().getSyllabus())
					.name(o.get().getCourse().getName())
					.ects(o.get().getCourse().getEcts())
					.teacher(TeacherDto.builder().id(o.get().getCourse().getTeacher().getId()).firstName(o.get().getCourse().getTeacher().getFirstName()).lastName(o.get().getCourse().getTeacher().getLastName()).build())
					.startDate(o.get().getCourse().getStartDate())
					.endDate(o.get().getCourse().getEndDate())
					.description(o.get().getCourse().getDescription())
					.teachingMaterials(o.get().getCourse().getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).publicationYear(nm.getPublicationYear()).publisher(nm.getPublisher()).description(nm.getDescription()).url(nm.getUrl()).outcome(nm.getOutcome()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet()))
					.build();

			CourseAnnouncementDto announcement = CourseAnnouncementDto.builder().id(o.get().getId()).title(o.get().getTitle()).content(o.get().getContent()).date(o.get().getDate()).imageUrl(o.get().getImage()).course(course).startDate(o.get().getStartDate()).endDate(o.get().getEndDate()).build();
			return new ResponseEntity<CourseAnnouncementDto>(announcement, HttpStatus.OK);
		}
		return new ResponseEntity<CourseAnnouncementDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<CourseAnnouncement> create(@Valid @RequestBody CourseAnnouncement r, Authentication authentication){
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
	public ResponseEntity<CourseAnnouncement> update(@PathVariable("id") Long id, @Valid @RequestBody CourseAnnouncement courseAnnouncement, Authentication authentication){

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
@RequestMapping(path = "/by-course/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<CourseAnnouncementDto>> getByCourse(@PathVariable("id") Long id, Authentication authentication){


		ArrayList<CourseAnnouncementDto> courseAnnouncements = new ArrayList<CourseAnnouncementDto>();
		for (CourseAnnouncement o : service.findAll()) {

			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();
				if (o.getCourse().getId().equals(id)) {
					CourseDto course = CourseDto.builder()
							.id(o.getCourse().getId())
							.courseCode(o.getCourse().getCourseCode())
							.syllabus(o.getCourse().getSyllabus())
							.name(o.getCourse().getName())
							.ects(o.getCourse().getEcts())
							.teacher(TeacherDto.builder().id(o.getCourse().getTeacher().getId()).firstName(o.getCourse().getTeacher().getFirstName()).lastName(o.getCourse().getTeacher().getLastName()).build())
							.startDate(o.getCourse().getStartDate())
							.endDate(o.getCourse().getEndDate())
							.description(o.getCourse().getDescription())
							.teachingMaterials(o.getCourse().getTeachingMaterials()
							.stream()
							.map( nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).publicationYear(nm.getPublicationYear()).publisher(nm.getPublisher()).description(nm.getDescription()).url(nm.getUrl()).outcome(nm.getOutcome()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet()))
							.build();

					Optional<Course> pr = courseService.findOne(o.getCourse().getId());
					List<Long> idlist = new ArrayList<>();
					for(Student s: pr.get().getStudents()) {
						idlist.add(s.getId());
					}

					if (idlist.contains(userId)) {
						courseAnnouncements.add(CourseAnnouncementDto.builder().id(o.getId()).title(o.getTitle()).content(o.getContent()).date(o.getDate()).imageUrl(o.getImage()).course(course).startDate(o.getStartDate()).endDate(o.getEndDate()).build());
					}else if(pr.get().getTeacher().getId() == userId) {
						courseAnnouncements.add(CourseAnnouncementDto.builder().id(o.getId()).title(o.getTitle()).content(o.getContent()).date(o.getDate()).imageUrl(o.getImage()).course(course).startDate(o.getStartDate()).endDate(o.getEndDate()).build());
					}
		}
				}
		return new ResponseEntity<Iterable<CourseAnnouncementDto>>(courseAnnouncements, HttpStatus.OK);
}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
}
