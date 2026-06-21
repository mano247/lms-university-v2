package com.lmsuniversity.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

import com.lmsuniversity.common.MyCoursesDto;
import com.lmsuniversity.course.CourseDto;
import com.lmsuniversity.course.CourseMapper;
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
		private StudentMapper mapper;

		@Autowired
		private CourseMapper courseMapper;

		@Autowired
		private StudyProgramService spService;

		@Autowired
		private ExamAttemptService examAttemptService;


		@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "", method = RequestMethod.GET)
		public ResponseEntity<Page<StudentListDto>> getAll(Pageable pageable) {
			Page<StudentListDto> students = service.findAll(pageable);
			return new ResponseEntity<Page<StudentListDto>>(students, HttpStatus.OK);
		}

		@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}", method = RequestMethod.GET)
		public ResponseEntity<StudentDto> get(@PathVariable("id") Long id) {
			Optional<Student> s = service.findOne(id);
			if (s.isPresent()) {
				return new ResponseEntity<StudentDto>(mapper.toDto(s.get()), HttpStatus.OK);
			}
			return new ResponseEntity<StudentDto>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "", method = RequestMethod.POST)
		public ResponseEntity<StudentDto> create(@Valid @RequestBody StudentCreateDto dto) {
			Student student = service.create(dto);
			return new ResponseEntity<StudentDto>(mapper.toDto(student), HttpStatus.CREATED);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
		@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
		public ResponseEntity<StudentDto> update(@PathVariable("id") Long id, @Valid @RequestBody StudentUpdateDto dto, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Student student = service.update(id, dto);
					if (student != null) {
						return new ResponseEntity<StudentDto>(mapper.toDto(student), HttpStatus.OK);
					}
				}
			}

			return new ResponseEntity<StudentDto>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/passed-exams", method = RequestMethod.GET)
		public ResponseEntity<List<MyCoursesDto>> getPassedExamAttempts(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Set<Course> actualCourses = service.findOne(id).get().getCourses();

					List<MyCoursesDto> result = new ArrayList<>();
					List<ExamAttempt> examAttempts = examAttemptService.findByStudentId(id);
					for (Course p : actualCourses) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId().equals(id) && pp.getCourse().getId().equals(p.getId()) && pp.getFinalGrade() > 5 && pp.getFinalGrade() <=10) {
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

					List<MyCoursesDto> result = new ArrayList<>();
					List<ExamAttempt> examAttempts = examAttemptService.findByStudentId(id);
					for (Course p : actualCourses) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId().equals(id) && pp.getCourse().getId().equals(p.getId()) && pp.getFinalGrade() ==5) {
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
					Set<CourseDto> courses = service.findOne(id).get().getCourses()
							.stream().map(courseMapper::toDto).collect(Collectors.toSet());

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
					Set<CourseDto> courses = service.findOne(id).get().getCourses()
							.stream()
							.filter(p -> p.getEndDate().after(new Date()))
							.filter(p -> p.getStartDate().before(new Date()))
							.map(courseMapper::toDto)
							.collect(Collectors.toSet());

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

					List<MyCoursesDto> result = new ArrayList<>();

					List<ExamAttempt> examAttempts = examAttemptService.findByStudentId(id);
					for (Course p : actualCourses) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId().equals(id) && pp.getCourse().getId().equals(p.getId()) && pp.getCourse().getId().equals(courseId) && pp.getFinalGrade() ==5) {
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
		public ResponseEntity<StudentDto> assignCoursesToStudent(@PathVariable("studyProgramId") Long studyProgramId, @Valid @RequestBody AssignCoursesDto dto, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				Student s = service.findOne(dto.getStudentId()).orElse(null);
				Optional<StudyProgram> optionalStudyProgram = spService.findOne(studyProgramId);

				if (s != null && optionalStudyProgram.isPresent()) {
					Set<Course> courses = new HashSet<>(optionalStudyProgram.get().getCourses());
					s.setCourses(courses);
					service.save(s);
					return new ResponseEntity<StudentDto>(mapper.toDto(s), HttpStatus.OK);
				}
				return new ResponseEntity<StudentDto>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<StudentDto>(HttpStatus.BAD_REQUEST);
		}


		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
		@RequestMapping(path = "/{id}/available-exams", method = RequestMethod.GET)
		public ResponseEntity<List<CourseDto>> getAvailableExams(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Set<Course> studentCourses = service.findOne(id).get().getCourses();

					List<CourseDto> result = new ArrayList<>();

					List<ExamAttempt> examAttempts = examAttemptService.findByStudentId(id);

					for (Course p : studentCourses) {
					    boolean coursePassed = false;

					    for (ExamAttempt pp : examAttempts) {
					        if (pp.getStudent().getId().equals(id) && pp.getCourse().getId().equals(p.getId())) {
					            if (pp.getFinalGrade() > 5) {
					                coursePassed = true;
					                break;
					            }
					        }
					    }
					    if (!coursePassed) {
					        result.add(courseMapper.toDto(p));
					    }
					}


					return new ResponseEntity<List<CourseDto>>(result, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
}
