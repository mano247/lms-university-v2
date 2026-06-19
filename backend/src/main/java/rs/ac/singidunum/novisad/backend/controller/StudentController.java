package rs.ac.singidunum.novisad.backend .controller;

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

import rs.ac.singidunum.novisad.backend.dto.MyCoursesDTO;
import rs.ac.singidunum.novisad.backend.dto.TeachingMaterialDTO;
import rs.ac.singidunum.novisad.backend.dto.TeacherDTO;
import rs.ac.singidunum.novisad.backend.dto.CourseDTO;
import rs.ac.singidunum.novisad.backend.dto.StudentDTO;
import rs.ac.singidunum.novisad.backend.dto.StudentUpdateDTO;
import rs.ac.singidunum.novisad.backend.dto.StudyProgramDTO;
import rs.ac.singidunum.novisad.backend.model.ExamAttempt;
import rs.ac.singidunum.novisad.backend.model.academic.Course;
import rs.ac.singidunum.novisad.backend.model.academic.StudyProgram;
import rs.ac.singidunum.novisad.backend.model.user.Student;
import rs.ac.singidunum.novisad.backend.security.services.UserDetailsImpl;
import rs.ac.singidunum.novisad.backend.service.PolaganjeService;
import rs.ac.singidunum.novisad.backend.service.StudentService;
import rs.ac.singidunum.novisad.backend.service.StudijskiProgramService;

@Controller
@RequestMapping(path = "/api/students")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {
		@Autowired
		private StudentService service;
		
		@Autowired
		private StudijskiProgramService spService;

		@Autowired
		private PolaganjeService polaganjaService;

		@Autowired
		PasswordEncoder encoder;


		@RequestMapping(path = "", method = RequestMethod.GET)
		public ResponseEntity<Iterable<StudentDTO>> getAll() {
			ArrayList<StudentDTO> student = new ArrayList<StudentDTO>();
			for (Student s : service.findAll()) {
				student.add(new StudentDTO(s.getId(), s.getClass().getSimpleName(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPassword(), s.getPermissions(), s.getIndexNumber(), s.getUsername()));
			}
			return new ResponseEntity<Iterable<StudentDTO>>(student, HttpStatus.OK);
		}

		@RequestMapping(path = "/{id}", method = RequestMethod.GET)
		public ResponseEntity<StudentDTO> get(@PathVariable("id") Long id) {
			Optional<Student> s = service.findOne(id);
			if (s.isPresent()) {
				StudentDTO dto = new StudentDTO(s.get().getId(), s.get().getClass().getSimpleName(), s.get().getFirstName(), s.get().getLastName(), s.get().getEmail(), s.get().getPassword(), s.get().getPermissions(), s.get().getIndexNumber(), s.get().getUsername());
				return new ResponseEntity<StudentDTO>(dto, HttpStatus.OK);
			}
			return new ResponseEntity<StudentDTO>(HttpStatus.NOT_FOUND);
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
		public ResponseEntity<Student> update(@PathVariable("id") Long id, @RequestBody StudentUpdateDTO student, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Student studentZaIzmenu = service.findOne(id).orElse(null);
					if (studentZaIzmenu != null) {
						studentZaIzmenu.setId(id);
						studentZaIzmenu.setFirstName(student.getFirstName());
						studentZaIzmenu.setLastName(student.getLastName());
						studentZaIzmenu.setEmail(student.getEmail());
						studentZaIzmenu.setPassword(studentZaIzmenu.getPassword());
						studentZaIzmenu.setPermissions(studentZaIzmenu.getPermissions());

						service.save(studentZaIzmenu);
						return new ResponseEntity<Student>(HttpStatus.OK);
					}
				}
			}

			return new ResponseEntity<Student>(HttpStatus.NOT_FOUND);
		}
		
		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/polozeniIspiti", method = RequestMethod.GET)
		public ResponseEntity<List<MyCoursesDTO>> getPolozeniIspiti(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
					Set<Course> praviPredmeti = service.findOne(id).get().getCourses();
					service.findOne(id).get().getCourses()
							.stream().map(p -> new CourseDTO(
									p.getId(),
									p.getCourseCode(),
									new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getName(),
									p.getEcts(),
									p.getDescription(),
									p.getSyllabus(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDTO(
													nm.getId(),
													nm.getTitle(),
													nm.getAuthors(),
													nm.getPageCount(),
													nm.getPublisher(),
													nm.getDescription(),
													nm.getQuantity(),
													nm.getIssuedQuantity()
											)).collect(Collectors.toSet()))).collect(Collectors.toSet());

					List<MyCoursesDTO> prikaz = new ArrayList<>();
					Iterable<ExamAttempt> examAttempts = polaganjaService.findAll();
					for (Course p : praviPredmeti) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId() && pp.getFinalGrade() > 5 && pp.getFinalGrade() <=10) {
								prikaz.add(new MyCoursesDTO(p.getId(), p.getName(), p.getDescription(), p.getSyllabus(),new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
										pp.getPoints(), p.getEcts(), pp.getFinalGrade()));
							}
						}
					}


					return new ResponseEntity<List<MyCoursesDTO>>(prikaz, HttpStatus.OK);
				}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/nepolozeniIspiti", method = RequestMethod.GET)
		public ResponseEntity<List<MyCoursesDTO>> getNPIspiti(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
					Set<Course> praviPredmeti = service.findOne(id).get().getCourses();
					service.findOne(id).get().getCourses()
							.stream().map(p -> new CourseDTO(
									p.getId(),
									p.getCourseCode(),
									new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getName(),
									p.getEcts(),
									p.getDescription(),
									p.getSyllabus(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDTO(
													nm.getId(),
													nm.getTitle(),
													nm.getAuthors(),
													nm.getPageCount(),
													nm.getPublisher(),
													nm.getDescription(),
													nm.getQuantity(),
													nm.getIssuedQuantity()
											)).collect(Collectors.toSet()))).collect(Collectors.toSet());

					List<MyCoursesDTO> prikaz = new ArrayList<>();
					Iterable<ExamAttempt> examAttempts = polaganjaService.findAll();
					for (Course p : praviPredmeti) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId() && pp.getFinalGrade() ==5) {
								prikaz.add(new MyCoursesDTO(p.getId(),p.getName(), p.getDescription(), p.getSyllabus(),new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
										pp.getPoints(), p.getEcts(), pp.getFinalGrade()));
							}
						}
					}


					return new ResponseEntity<List<MyCoursesDTO>>(prikaz, HttpStatus.OK);
				}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/sviIspiti", method = RequestMethod.GET)
		public ResponseEntity<Set<CourseDTO>> getSviPredmeti(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					service.findOne(id).get().getCourses();
					Set<CourseDTO> courses = service.findOne(id).get().getCourses()
							.stream().map(p -> new CourseDTO(
									p.getId(),
									p.getCourseCode(),
									new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getSyllabus(),
									p.getName(),
									p.getEcts(),
									new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									p.getStartDate(),
									p.getEndDate(),
									p.getDescription(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDTO(
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

					return new ResponseEntity<Set<CourseDTO>>(courses, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/sviAPredmeti", method = RequestMethod.GET)
		public ResponseEntity<Set<CourseDTO>> getSviAktivniPredmeti(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					service.findOne(id).get().getCourses();
					Set<CourseDTO> courses = service.findOne(id).get().getCourses()
							.stream().filter(p -> p.getEndDate().after(new Date())).filter(p -> p.getStartDate().before(new Date())).map(p -> new CourseDTO(
									p.getId(),
									p.getCourseCode(),
									new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getSyllabus(),
									p.getName(),
									p.getEcts(),
									new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									p.getStartDate(),
									p.getEndDate(),
									p.getDescription(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDTO(
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

					return new ResponseEntity<Set<CourseDTO>>(courses, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/{id}/nepolozeniIspiti/{id_predmeta}", method = RequestMethod.GET)
		public ResponseEntity<List<MyCoursesDTO>> getNepolozeniIspitiPoPredmetu(@PathVariable("id") Long id,@PathVariable("id_predmeta") Long id_predmta, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Set<Course> praviPredmeti = service.findOne(id).get().getCourses();
					service.findOne(id).get().getCourses()
							.stream().map(p -> new CourseDTO(
									p.getId(),
									p.getCourseCode(),
									new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getName(),
									p.getEcts(),
									p.getDescription(),
									p.getSyllabus(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDTO(
													nm.getId(),
													nm.getTitle(),
													nm.getAuthors(),
													nm.getPageCount(),
													nm.getPublisher(),
													nm.getDescription(),
													nm.getQuantity(),
													nm.getIssuedQuantity()
											)).collect(Collectors.toSet()))).collect(Collectors.toSet());

					List<MyCoursesDTO> prikaz = new ArrayList<>();
					
					Iterable<ExamAttempt> examAttempts = polaganjaService.findAll();
					for (Course p : praviPredmeti) {
						for (ExamAttempt pp : examAttempts) {
							if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId() && pp.getCourse().getId().equals(id_predmta) && pp.getFinalGrade() ==5) {
								prikaz.add(new MyCoursesDTO(p.getId(),p.getName(), p.getDescription(), p.getSyllabus(), new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
										pp.getPoints(), p.getEcts(), pp.getFinalGrade()));
							}
						}
					}


					return new ResponseEntity<List<MyCoursesDTO>>(prikaz, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION','ADMINISTRATOR_PERMISSION')")
		@RequestMapping(path = "/dsp/{smer_id}", method = RequestMethod.PUT)
			public ResponseEntity<Student> DodaliStudentuPredmete(@PathVariable("smer_id") Long smer_id, @RequestBody Student student, Authentication authentication) {

			if (authentication.isAuthenticated()) {	
				Student s = service.findOne(student.getId()).orElse(null);
					Optional<StudyProgram> optionalStudijskiProgram = spService.findOne(smer_id);
				    StudyProgram studyProgram = optionalStudijskiProgram.get();
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
		public ResponseEntity<List<CourseDTO>> getIspitiZaPrijavu(@PathVariable("id") Long id, Authentication authentication) {
			if (authentication.isAuthenticated()) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				Long userId = userDetails.getId();

				if (id.equals(userId)) {
					Set<Course> predmetiStudenta = service.findOne(id).get().getCourses();
					
					service.findOne(id).get().getCourses()
							.stream().map(p -> new CourseDTO(
									p.getId(),
									p.getCourseCode(),
									new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),
									new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()),
									p.getName(),
									p.getEcts(),
									p.getDescription(),
									p.getSyllabus(),
									p.getTeachingMaterials().stream().map(nm ->
											new TeachingMaterialDTO(
													nm.getId(),
													nm.getTitle(),
													nm.getAuthors(),
													nm.getPageCount(),
													nm.getPublisher(),
													nm.getDescription(),
													nm.getQuantity(),
													nm.getIssuedQuantity()
											)).collect(Collectors.toSet()))).collect(Collectors.toSet());

					List<CourseDTO> prikaz = new ArrayList<>();

					Iterable<ExamAttempt> examAttempts = polaganjaService.findAll();

					for (Course p : predmetiStudenta) {
					    boolean polozenPredmet = false;

					    for (ExamAttempt pp : examAttempts) {
					        if (pp.getStudent().getId() == id && pp.getCourse().getId() == p.getId()) {
					            if (pp.getFinalGrade() > 5) {
					                polozenPredmet = true;
					                break;
					            }
					        }
					    }
					    if (!polozenPredmet) {
					        prikaz.add(new CourseDTO(p.getId(), p.getCourseCode(),
					                new TeacherDTO(p.getTeacher().getId(), p.getTeacher().getFirstName(), p.getTeacher().getLastName()),
					                new StudyProgramDTO(p.getStudyProgram().getId(), p.getStudyProgram().getProgramCode(), p.getStudyProgram().getName()),
					                p.getName(), p.getEcts(), p.getDescription(), p.getSyllabus()));
					    }
					}


					return new ResponseEntity<List<CourseDTO>>(prikaz, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
}
