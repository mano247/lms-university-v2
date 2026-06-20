package com.lmsuniversity.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lmsuniversity.common.MyCoursesDto;
import com.lmsuniversity.teachingmaterial.TeachingMaterialDto;
import com.lmsuniversity.course.CourseDto;
import com.lmsuniversity.common.StudentUpdateDto;
import com.lmsuniversity.studyprogram.StudyProgramDto;
import com.lmsuniversity.examattempt.ExamAttempt;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.studyprogram.StudyProgram;
import com.lmsuniversity.security.services.UserDetailsImpl;
import com.lmsuniversity.examattempt.ExamAttemptService;
import com.lmsuniversity.studyprogram.StudyProgramService;

@Controller
@RequestMapping(path = "/api/students")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {
		@Autowired
		private StudentService service;

		@Autowired
		private StudyProgramService spService;

		@Autowired
		private ExamAttemptService examAttemptService;

		@Autowired
		PasswordEncoder encoder;


		@RequestMapping(path = "", method = RequestMethod.GET)
		public ResponseEntity<Iterable<StudentDto>> getAll() {
			ArrayList<StudentDto> student = new ArrayList<StudentDto>();
			for (Student s : service.findAll()) {
				student.add(new StudentDto(s.getId(), s.getClass().getSimpleName(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPassword(), s.getPermissions(), s.getIndexNumber(), s.getUsername()));
			}
			return new ResponseEntity<Iterable<StudentDto>>(student, HttpStatus.OK);
		}

		@RequestMapping(path = "/{id}", method = RequestMethod.GET)
		public ResponseEntity<StudentDto> get(@PathVariable("id") Long id) {
			Optional<Student> s = service.findOne(id);
			if (s.isPresent()) {
				StudentDto dto = new StudentDto(s.get().getId(), s.get().getClass().getSimpleName(), s.get().getFirstName(), s.get().getLastName(), s.get().getEmail(), s.get().getPassword(), s.get().getPermissions(), s.get().getIndexNumber(), s.get().getUsername());
				return new ResponseEntity<StudentDto>(dto, HttpStatus.OK);
			}
			return new ResponseEntity<StudentDto>(HttpStatus.NOT_FOUND);
		}

		@RequestMapping(path = "", method = RequestMethod.POST)
		public ResponseEntity<Student> create(@RequestBody Student r) {
			try {
				service.save(r);
				return new ResponseEntity<Student>(r, HttpStatus.CREATED);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ResponseEntity<Student>(HttpStatus.BAD_REQUEST);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
		@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
		public ResponseEntity<Student> update(@PathVariable("id") Long id, @RequestBody StudentUpdateDto student, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Student studentToUpdate = service.findOne(id).orElse(null);
					if (studentToUpdate != null) {
						studentToUpdate.setId(id);
						studentToUpdate.setFirstName(student.getFirstName());
						studentToUpdate.setLastName(student.getLastName());
						studentToUpdate.setEmail(student.getEmail());
						studentToUpdate.setPassword(studentToUpdate.getPassword());
						studentToUpdate.setPermissions(studentToUpdate.getPermissions());

						service.save(studentToUpdate);
						return new ResponseEntity<Student>(HttpStatus.OK);
					}
				}
			}

			return new ResponseEntity<Student>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/polozeniIspiti", method = RequestMethod.GET)
		public ResponseEntity<List<MyCoursesDto>> getPassedExamAttempts(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
					Set<Course> actualCourses = service.findOne(id).get().getCourses();
					service.findOne(id).get().getCourses()
							.stream().map(p -> new CourseDto(
									p.getId(),
									p.getCourseCode(),
									new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									new StudyProgramDto(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getName(),
									p.getEcts(),
									p.getDescription(),
									p.getSyllabus(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDto(
													nm.getId(),
													nm.getTitle(),
													nm.getAuthors(),
													nm.getPageCount(),
													nm.getPublisher(),
													nm.getDescription(),
													nm.getQuantity(),
													nm.getIssuedQuantity()
											)).collect(Collectors.toSet()))).collect(Collectors.toSet());

					List<MyCoursesDto> result = new ArrayList<>();
					Iterable<ExamAttempt> examAttempts = examAttemptService.findAll();
					for (Course p : actualCourses) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId() && pp.getFinalGrade() > 5 && pp.getFinalGrade() <=10) {
								result.add(new MyCoursesDto(p.getId(), p.getName(), p.getDescription(), p.getSyllabus(),new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
										pp.getPoints(), p.getEcts(), pp.getFinalGrade()));
							}
						}
					}


					return new ResponseEntity<List<MyCoursesDto>>(result, HttpStatus.OK);
				}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/nepolozeniIspiti", method = RequestMethod.GET)
		public ResponseEntity<List<MyCoursesDto>> getFailedExamAttempts(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
					Set<Course> actualCourses = service.findOne(id).get().getCourses();
					service.findOne(id).get().getCourses()
							.stream().map(p -> new CourseDto(
									p.getId(),
									p.getCourseCode(),
									new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									new StudyProgramDto(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getName(),
									p.getEcts(),
									p.getDescription(),
									p.getSyllabus(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDto(
													nm.getId(),
													nm.getTitle(),
													nm.getAuthors(),
													nm.getPageCount(),
													nm.getPublisher(),
													nm.getDescription(),
													nm.getQuantity(),
													nm.getIssuedQuantity()
											)).collect(Collectors.toSet()))).collect(Collectors.toSet());

					List<MyCoursesDto> result = new ArrayList<>();
					Iterable<ExamAttempt> examAttempts = examAttemptService.findAll();
					for (Course p : actualCourses) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId() && pp.getFinalGrade() ==5) {
								result.add(new MyCoursesDto(p.getId(),p.getName(), p.getDescription(), p.getSyllabus(),new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
										pp.getPoints(), p.getEcts(), pp.getFinalGrade()));
							}
						}
					}


					return new ResponseEntity<List<MyCoursesDto>>(result, HttpStatus.OK);
				}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/sviIspiti", method = RequestMethod.GET)
		public ResponseEntity<Set<CourseDto>> getAllCourses(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					service.findOne(id).get().getCourses();
					Set<CourseDto> courses = service.findOne(id).get().getCourses()
							.stream().map(p -> new CourseDto(
									p.getId(),
									p.getCourseCode(),
									new StudyProgramDto(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getSyllabus(),
									p.getName(),
									p.getEcts(),
									new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									p.getStartDate(),
									p.getEndDate(),
									p.getDescription(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDto(
													nm.getId(),
													nm.getTitle(),
													nm.getAuthors(),
													nm.getPageCount(),
													nm.getPublisher(),
													nm.getDescription(),
													nm.getQuantity(),
													nm.getIssuedQuantity(),
													nm.getPublicationYear()
											)).collect(Collectors.toSet()))).collect(Collectors.toSet());

					return new ResponseEntity<Set<CourseDto>>(courses, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/sviAPredmeti", method = RequestMethod.GET)
		public ResponseEntity<Set<CourseDto>> getAllActiveCourses(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					service.findOne(id).get().getCourses();
					Set<CourseDto> courses = service.findOne(id).get().getCourses()
							.stream().filter(p -> p.getEndDate().after(new Date())).filter(p -> p.getStartDate().before(new Date())).map(p -> new CourseDto(
									p.getId(),
									p.getCourseCode(),
									new StudyProgramDto(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getSyllabus(),
									p.getName(),
									p.getEcts(),
									new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									p.getStartDate(),
									p.getEndDate(),
									p.getDescription(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDto(
													nm.getId(),
													nm.getTitle(),
													nm.getAuthors(),
													nm.getPageCount(),
													nm.getPublisher(),
													nm.getDescription(),
													nm.getQuantity(),
													nm.getIssuedQuantity(),
													nm.getPublicationYear()
											)).collect(Collectors.toSet()))).collect(Collectors.toSet());

					return new ResponseEntity<Set<CourseDto>>(courses, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/nepolozeniIspiti/{id_predmeta}", method = RequestMethod.GET)
		public ResponseEntity<List<MyCoursesDto>> getFailedExamAttemptsByCourse(@PathVariable("id") Long id,@PathVariable("id_predmeta") Long courseId, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Set<Course> actualCourses = service.findOne(id).get().getCourses();
					service.findOne(id).get().getCourses()
							.stream().map(p -> new CourseDto(
									p.getId(),
									p.getCourseCode(),
									new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									new StudyProgramDto(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getName(),
									p.getEcts(),
									p.getDescription(),
									p.getSyllabus(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDto(
													nm.getId(),
													nm.getTitle(),
													nm.getAuthors(),
													nm.getPageCount(),
													nm.getPublisher(),
													nm.getDescription(),
													nm.getQuantity(),
													nm.getIssuedQuantity()
											)).collect(Collectors.toSet()))).collect(Collectors.toSet());

					List<MyCoursesDto> result = new ArrayList<>();

					Iterable<ExamAttempt> examAttempts = examAttemptService.findAll();
					for (Course p : actualCourses) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId() && pp.getCourse().getId().equals(courseId) && pp.getFinalGrade() ==5) {
								result.add(new MyCoursesDto(p.getId(),p.getName(), p.getDescription(), p.getSyllabus(), new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
										pp.getPoints(), p.getEcts(), pp.getFinalGrade()));
							}
						}
					}


					return new ResponseEntity<List<MyCoursesDto>>(result, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION','ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/dsp/{smer_id}", method = RequestMethod.PUT)
			public ResponseEntity<Student> assignCoursesToStudent(@PathVariable("smer_id") Long studyProgramId, @RequestBody Student student, Authentication authentication) {

			if (authentication.isAuthenticated()) {
				Student s = service.findOne(student.getId()).orElse(null);
					Optional<StudyProgram> optionalStudyProgram = spService.findOne(studyProgramId);
				    StudyProgram studyProgram = optionalStudyProgram.get();
				    Set<Course> courses = new HashSet<>();
				    for (Course p : studyProgram.getCourses()) {
					    courses.add(p);
				    }

					if (s != null) {
							s.setId(student.getId());
							s.setCourses(courses);

							service.save(s);
							return new ResponseEntity<Student>(s,HttpStatus.OK);
							}
					return new ResponseEntity<Student>(HttpStatus.NOT_FOUND);
					}
			return new ResponseEntity<Student>(HttpStatus.BAD_REQUEST);
			}


		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
		@RequestMapping(path = "/{id}/ispitiZaPrijavu", method = RequestMethod.GET)
		public ResponseEntity<List<CourseDto>> getAvailableExams(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Set<Course> studentCourses = service.findOne(id).get().getCourses();

					service.findOne(id).get().getCourses()
							.stream().map(p -> new CourseDto(
									p.getId(),
									p.getCourseCode(),
									new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									new StudyProgramDto(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getName(),
									p.getEcts(),
									p.getDescription(),
									p.getSyllabus(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDto(
													nm.getId(),
													nm.getTitle(),
													nm.getAuthors(),
													nm.getPageCount(),
													nm.getPublisher(),
													nm.getDescription(),
													nm.getQuantity(),
													nm.getIssuedQuantity()
											)).collect(Collectors.toSet()))).collect(Collectors.toSet());

					List<CourseDto> result = new ArrayList<>();

					Iterable<ExamAttempt> examAttempts = examAttemptService.findAll();

					for (Course p : studentCourses) {
					    boolean coursePassed = false;

					    for (ExamAttempt pp : examAttempts) {
					        if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId()) {
					            if (pp.getFinalGrade() > 5) {
					                coursePassed = true;
					                break;
					            }
					        }
					    }
					    if (!coursePassed) {
					        result.add(new CourseDto(p.getId(), p.getCourseCode(),
					                new TeacherDto(p.getTeacher().getId(), p.getTeacher().getFirstName(), p.getTeacher().getLastName()),
					                new StudyProgramDto(p.getStudyProgram().getId(), p.getStudyProgram().getProgramCode(), p.getStudyProgram().getName()),
					                p.getName(), p.getEcts(), p.getDescription(), p.getSyllabus()));
					    }
					}


					return new ResponseEntity<List<CourseDto>>(result, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
}
