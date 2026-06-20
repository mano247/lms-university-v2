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
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
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
public class StudentController {
		@Autowired
		private StudentService service;

		@Autowired
		private StudyProgramService spService;

		@Autowired
		private ExamAttemptService examAttemptService;

		@Autowired
		PasswordEncoder encoder;


		@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "", method = RequestMethod.GET)
		public ResponseEntity<Iterable<StudentDto>> getAll() {
			ArrayList<StudentDto> student = new ArrayList<StudentDto>();
			for (Student s : service.findAll()) {
				student.add(StudentDto.builder().id(s.getId()).userType(s.getClass().getSimpleName()).firstName(s.getFirstName()).lastName(s.getLastName()).email(s.getEmail()).password(s.getPassword()).permission(s.getPermissions()).indexNumber(s.getIndexNumber()).username(s.getUsername()).build());
			}
			return new ResponseEntity<Iterable<StudentDto>>(student, HttpStatus.OK);
		}

		@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}", method = RequestMethod.GET)
		public ResponseEntity<StudentDto> get(@PathVariable("id") Long id) {
			Optional<Student> s = service.findOne(id);
			if (s.isPresent()) {
				StudentDto dto = StudentDto.builder().id(s.get().getId()).userType(s.get().getClass().getSimpleName()).firstName(s.get().getFirstName()).lastName(s.get().getLastName()).email(s.get().getEmail()).password(s.get().getPassword()).permission(s.get().getPermissions()).indexNumber(s.get().getIndexNumber()).username(s.get().getUsername()).build();
				return new ResponseEntity<StudentDto>(dto, HttpStatus.OK);
			}
			return new ResponseEntity<StudentDto>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "", method = RequestMethod.POST)
		public ResponseEntity<Student> create(@Valid @RequestBody Student r) {
			try {
				r.setPassword(encoder.encode(r.getPassword()));
				service.save(r);
				return new ResponseEntity<Student>(r, HttpStatus.CREATED);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ResponseEntity<Student>(HttpStatus.BAD_REQUEST);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
		@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
		public ResponseEntity<Student> update(@PathVariable("id") Long id, @Valid @RequestBody StudentUpdateDto student, Authentication authentication) {
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
		@RequestMapping(path = "/{id}/passed-exams", method = RequestMethod.GET)
		public ResponseEntity<List<MyCoursesDto>> getPassedExamAttempts(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Set<Course> actualCourses = service.findOne(id).get().getCourses();
					service.findOne(id).get().getCourses()
							.stream().map(p -> CourseDto.builder()
									.id(p.getId())
									.courseCode(p.getCourseCode())
									.teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build())
									.studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build())
									.name(p.getName())
									.ects(p.getEcts())
									.description(p.getDescription())
									.syllabus(p.getSyllabus())
									.teachingMaterials(p.getTeachingMaterials().stream().map(nm ->
											TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet()))
									.build()).collect(Collectors.toSet());

					List<MyCoursesDto> result = new ArrayList<>();
					Iterable<ExamAttempt> examAttempts = examAttemptService.findAll();
					for (Course p : actualCourses) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId() && pp.getFinalGrade() > 5 && pp.getFinalGrade() <=10) {
								result.add(MyCoursesDto.builder().id(p.getId()).courseName(p.getName()).description(p.getDescription()).syllabus(p.getSyllabus()).teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build()).points(pp.getPoints()).ects(p.getEcts()).grade(pp.getFinalGrade()).build());
							}
						}
					}


					return new ResponseEntity<List<MyCoursesDto>>(result, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/failed-exams", method = RequestMethod.GET)
		public ResponseEntity<List<MyCoursesDto>> getFailedExamAttempts(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Set<Course> actualCourses = service.findOne(id).get().getCourses();
					service.findOne(id).get().getCourses()
							.stream().map(p -> CourseDto.builder()
									.id(p.getId())
									.courseCode(p.getCourseCode())
									.teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build())
									.studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build())
									.name(p.getName())
									.ects(p.getEcts())
									.description(p.getDescription())
									.syllabus(p.getSyllabus())
									.teachingMaterials(p.getTeachingMaterials().stream().map(nm ->
											TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet()))
									.build()).collect(Collectors.toSet());

					List<MyCoursesDto> result = new ArrayList<>();
					Iterable<ExamAttempt> examAttempts = examAttemptService.findAll();
					for (Course p : actualCourses) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId() && pp.getFinalGrade() ==5) {
								result.add(MyCoursesDto.builder().id(p.getId()).courseName(p.getName()).description(p.getDescription()).syllabus(p.getSyllabus()).teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build()).points(pp.getPoints()).ects(p.getEcts()).grade(pp.getFinalGrade()).build());
							}
						}
					}


					return new ResponseEntity<List<MyCoursesDto>>(result, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/all-courses", method = RequestMethod.GET)
		public ResponseEntity<Set<CourseDto>> getAllCourses(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					service.findOne(id).get().getCourses();
					Set<CourseDto> courses = service.findOne(id).get().getCourses()
							.stream().map(p -> CourseDto.builder().id(p.getId()).courseCode(p.getCourseCode()).studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build()).syllabus(p.getSyllabus()).name(p.getName()).ects(p.getEcts()).teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build()).startDate(p.getStartDate()).endDate(p.getEndDate()).description(p.getDescription()).teachingMaterials(p.getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).publicationYear(nm.getPublicationYear()).build()).collect(Collectors.toSet())).build()).collect(Collectors.toSet());

					return new ResponseEntity<Set<CourseDto>>(courses, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/active-courses", method = RequestMethod.GET)
		public ResponseEntity<Set<CourseDto>> getAllActiveCourses(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					service.findOne(id).get().getCourses();
					Set<CourseDto> courses = service.findOne(id).get().getCourses()
							.stream().filter(p -> p.getEndDate().after(new Date())).filter(p -> p.getStartDate().before(new Date())).map(p -> CourseDto.builder().id(p.getId()).courseCode(p.getCourseCode()).studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build()).syllabus(p.getSyllabus()).name(p.getName()).ects(p.getEcts()).teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build()).startDate(p.getStartDate()).endDate(p.getEndDate()).description(p.getDescription()).teachingMaterials(p.getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).publicationYear(nm.getPublicationYear()).build()).collect(Collectors.toSet())).build()).collect(Collectors.toSet());

					return new ResponseEntity<Set<CourseDto>>(courses, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/failed-exams/{courseId}", method = RequestMethod.GET)
		public ResponseEntity<List<MyCoursesDto>> getFailedExamAttemptsByCourse(@PathVariable("id") Long id,@PathVariable("courseId") Long courseId, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Set<Course> actualCourses = service.findOne(id).get().getCourses();
					service.findOne(id).get().getCourses()
							.stream().map(p -> CourseDto.builder()
									.id(p.getId())
									.courseCode(p.getCourseCode())
									.teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build())
									.studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build())
									.name(p.getName())
									.ects(p.getEcts())
									.description(p.getDescription())
									.syllabus(p.getSyllabus())
									.teachingMaterials(p.getTeachingMaterials().stream().map(nm ->
											TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet()))
									.build()).collect(Collectors.toSet());

					List<MyCoursesDto> result = new ArrayList<>();

					Iterable<ExamAttempt> examAttempts = examAttemptService.findAll();
					for (Course p : actualCourses) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId() && pp.getCourse().getId().equals(courseId) && pp.getFinalGrade() ==5) {
								result.add(MyCoursesDto.builder().id(p.getId()).courseName(p.getName()).description(p.getDescription()).syllabus(p.getSyllabus()).teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build()).points(pp.getPoints()).ects(p.getEcts()).grade(pp.getFinalGrade()).build());
							}
						}
					}


					return new ResponseEntity<List<MyCoursesDto>>(result, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION','ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/assign-courses/{studyProgramId}", method = RequestMethod.PUT)
			public ResponseEntity<Student> assignCoursesToStudent(@PathVariable("studyProgramId") Long studyProgramId, @RequestBody Student student, Authentication authentication) {

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
		@RequestMapping(path = "/{id}/available-exams", method = RequestMethod.GET)
		public ResponseEntity<List<CourseDto>> getAvailableExams(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Set<Course> studentCourses = service.findOne(id).get().getCourses();

					service.findOne(id).get().getCourses()
							.stream().map(p -> CourseDto.builder()
									.id(p.getId())
									.courseCode(p.getCourseCode())
									.teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build())
									.studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build())
									.name(p.getName())
									.ects(p.getEcts())
									.description(p.getDescription())
									.syllabus(p.getSyllabus())
									.teachingMaterials(p.getTeachingMaterials().stream().map(nm ->
											TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet()))
									.build()).collect(Collectors.toSet());

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
					        result.add(CourseDto.builder().id(p.getId()).courseCode(p.getCourseCode()).teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build()).studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build()).name(p.getName()).ects(p.getEcts()).description(p.getDescription()).syllabus(p.getSyllabus()).build());
					    }
					}


					return new ResponseEntity<List<CourseDto>>(result, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
}
