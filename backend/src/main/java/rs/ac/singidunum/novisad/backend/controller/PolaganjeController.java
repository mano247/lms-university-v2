package rs.ac.singidunum.novisad.backend.controller;

import java.util.ArrayList;
import java.util.List;
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

import rs.ac.singidunum.novisad.backend.dto.AddGradeDTO;
import rs.ac.singidunum.novisad.backend.dto.TeachingMaterialDTO;
import rs.ac.singidunum.novisad.backend.dto.TeacherDTO;
import rs.ac.singidunum.novisad.backend.dto.ExamAttemptDTO;
import rs.ac.singidunum.novisad.backend.dto.CourseDTO;
import rs.ac.singidunum.novisad.backend.dto.StudentDTO;
import rs.ac.singidunum.novisad.backend.dto.StudyProgramDTO;
import rs.ac.singidunum.novisad.backend.dto.TeacherStudentsDTO;
import rs.ac.singidunum.novisad.backend.model.ExamAttempt;
import rs.ac.singidunum.novisad.backend.model.academic.Course;
import rs.ac.singidunum.novisad.backend.model.user.Teacher;
import rs.ac.singidunum.novisad.backend.model.user.Student;
import rs.ac.singidunum.novisad.backend.security.services.UserDetailsImpl;
import rs.ac.singidunum.novisad.backend.service.NastavnikService;
import rs.ac.singidunum.novisad.backend.service.PolaganjeService;

@Controller
@RequestMapping(path = "/api/examAttempts")
@CrossOrigin(origins = "http://localhost:4200")
public class PolaganjeController {

	@Autowired
	private PolaganjeService service;

	@Autowired
	private NastavnikService profesorService;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<ExamAttemptDTO>> getAll() {
		ArrayList<ExamAttemptDTO> pokusaji = new ArrayList<ExamAttemptDTO>();
		for (ExamAttempt pp : service.findAll()) {
			Course p = pp.getCourse();
			Student s = pp.getStudent();
			Teacher n = pp.getTeacher();
			
			Set<TeachingMaterialDTO> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> new TeachingMaterialDTO(
					nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
					nm.getQuantity(),
					nm.getIssuedQuantity()
			)).collect(Collectors.toSet());
			CourseDTO course = new CourseDTO(p.getId(),p.getCourseCode(), new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()), p.getName(), p.getEcts(), p.getDescription(), p.getSyllabus(), teachingMaterials);
			StudentDTO student = new StudentDTO(s.getId(), s.getClass().getSimpleName(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPassword(), s.getPermissions(), s.getIndexNumber(), s.getUsername());
			TeacherDTO teacher = new TeacherDTO(n.getId(), n.getClass().getSimpleName(), n.getFirstName(), n.getLastName(), n.getEmail(), n.getPassword());


			pokusaji.add(new ExamAttemptDTO(pp.getId(), pp.getPoints(), pp.getFinalGrade(), pp.getStartTime(), pp.getEndTime(), pp.getNote(),student, course, teacher));
		}
		return new ResponseEntity<Iterable<ExamAttemptDTO>>(pokusaji, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ExamAttemptDTO> get(@PathVariable("id") Long id) {
		Optional<ExamAttempt> pp = service.findOne(id);
		if (pp.isPresent()) {
			Course p = pp.get().getCourse();
			Student s = pp.get().getStudent();
			Teacher n = pp.get().getTeacher();
			Set<TeachingMaterialDTO> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> new TeachingMaterialDTO(
					nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
					nm.getQuantity(),
					nm.getIssuedQuantity()
			)).collect(Collectors.toSet());
			CourseDTO course = new CourseDTO(p.getId(),p.getCourseCode(),  new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()), new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()), p.getName(), p.getEcts(), p.getDescription(), p.getSyllabus(), teachingMaterials);
			StudentDTO student = new StudentDTO(s.getId(), s.getClass().getSimpleName(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPassword(), s.getPermissions(), s.getIndexNumber(), s.getUsername());
			TeacherDTO teacher = new TeacherDTO(n.getId(), n.getClass().getSimpleName(), n.getFirstName(), n.getLastName(), n.getEmail(), n.getPassword());
			
			ExamAttemptDTO pokusaj = new ExamAttemptDTO(pp.get().getId(), pp.get().getPoints(), pp.get().getFinalGrade(),pp.get().getStartTime(),pp.get().getEndTime(),pp.get().getNote(),student, course,  teacher);
			return new ResponseEntity<ExamAttemptDTO>(pokusaj, HttpStatus.OK);
		}
		return new ResponseEntity<ExamAttemptDTO>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
	@RequestMapping(path = "/c", method = RequestMethod.POST)
	public ResponseEntity<ExamAttempt> create(@RequestBody ExamAttempt p){
		try {
			service.save(p);
			return new ResponseEntity<ExamAttempt>(p, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<ExamAttempt>(HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ExamAttempt> update(@PathVariable("id") Long id, @RequestBody ExamAttempt pokusajPolaganja) {

		ExamAttempt u = service.findOne(id).orElse(null);
		if (u != null) {

			pokusajPolaganja.setId(id);
			if (pokusajPolaganja.getPoints() == 0) {
				pokusajPolaganja.setPoints(u.getPoints());
			}
			if(pokusajPolaganja.getFinalGrade() == 0) {
				pokusajPolaganja.setFinalGrade(u.getFinalGrade());
			}
			pokusajPolaganja.setCourse(u.getCourse());
			pokusajPolaganja.setStudent(u.getStudent());
			pokusajPolaganja.setTeacher(u.getTeacher());
			service.save(pokusajPolaganja);

			return new ResponseEntity<ExamAttempt> (HttpStatus.OK);
				}

		return new ResponseEntity<ExamAttempt>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION','ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ExamAttempt> delete(@PathVariable("id") Long id) {
		if (service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<ExamAttempt>(HttpStatus.OK);
		}
		return new ResponseEntity<ExamAttempt>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "/dobaviNastavniku/{idNastavnika}", method = RequestMethod.GET)
	public ResponseEntity<List<AddGradeDTO>> getPredmeteProfesora(
			@PathVariable("idNastavnika") Long idProfesora,
			Authentication authentication
	) {
		if(authentication.isAuthenticated()) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Long userId = userDetails.getId();

			if (idProfesora.equals(userId)) {

				Set<Course> profesoroviPredmeti = profesorService.findOne(idProfesora).get().getCourses();

				List<AddGradeDTO> response = new ArrayList<>();
				for(Course p : profesoroviPredmeti) {

					List<TeacherStudentsDTO>  students = new ArrayList<TeacherStudentsDTO>();
					for(Student s : p.getStudents()) {

						for(ExamAttempt pp : service.findAll()) {

							if(pp.getCourse().getId().equals(p.getId()) && pp.getStudent().getId() == s.getId()) {

								TeacherStudentsDTO student = new TeacherStudentsDTO(s.getId(), pp.getId(), s.getFirstName(), s.getLastName(), s.getIndexNumber(), pp.getPoints(), pp.getFinalGrade());
								students.add(student);
								break;

							}
						}


					}
					if(students.size() > 0 ) {
						response.add(new AddGradeDTO(
								p.getId(), p.getName(), p.getEcts(), p.getSyllabus(), students));
					}
				}
				return new ResponseEntity<List<AddGradeDTO>>(response, HttpStatus.OK);

			}
		}

		return new ResponseEntity<List<AddGradeDTO>>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = "prijavljeni/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<ExamAttemptDTO>> getPrijavljeniIspiti(@PathVariable("id") Long id) {
		ArrayList<ExamAttemptDTO> pokusaji = new ArrayList<ExamAttemptDTO>();
		for (ExamAttempt pp : service.findAll()) {
			if(pp.getFinalGrade()==0 && pp.getPoints()==0.0 && pp.getStudent().getId()==id) {
				Course p = pp.getCourse();
				Student s = pp.getStudent();
				Teacher n = pp.getTeacher();
				
				Set<TeachingMaterialDTO> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> new TeachingMaterialDTO(
						nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
						nm.getQuantity(),
						nm.getIssuedQuantity()
				)).collect(Collectors.toSet());
				CourseDTO course = new CourseDTO(p.getId(),p.getCourseCode(),  new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()), new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()), p.getName(), p.getEcts(), p.getDescription(), p.getSyllabus(), teachingMaterials);
				StudentDTO student = new StudentDTO(s.getId(), s.getClass().getSimpleName(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPassword(), s.getPermissions(), s.getIndexNumber(), s.getUsername());
				TeacherDTO teacher = new TeacherDTO(n.getId(), n.getClass().getSimpleName(), n.getFirstName(), n.getLastName(), n.getEmail(), n.getPassword());
	
	
				pokusaji.add(new ExamAttemptDTO(pp.getId(), pp.getPoints(), pp.getFinalGrade(), pp.getStartTime(), pp.getEndTime(), pp.getNote(),student, course, teacher));
		}}
		return new ResponseEntity<Iterable<ExamAttemptDTO>>(pokusaji, HttpStatus.OK);
	}
	
	@RequestMapping(path = "prijavljeniPoPredmetu/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<ExamAttemptDTO>> getPrijavljeniIspitiPoPredmetu(@PathVariable("id") Long id) {
		ArrayList<ExamAttemptDTO> pokusaji = new ArrayList<ExamAttemptDTO>();
		for (ExamAttempt pp : service.findAll()) {
			if(pp.getFinalGrade()==0 && pp.getPoints()==0.0 && pp.getCourse().getId().equals(id)) {
				Course p = pp.getCourse();
				Student s = pp.getStudent();
				Teacher n = pp.getTeacher();
				
				Set<TeachingMaterialDTO> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> new TeachingMaterialDTO(
						nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
						nm.getQuantity(),
						nm.getIssuedQuantity()
				)).collect(Collectors.toSet());
				CourseDTO course = new CourseDTO(p.getId(),p.getCourseCode(),  new TeacherDTO(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()), new StudyProgramDTO(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()), p.getName(), p.getEcts(), p.getDescription(), p.getSyllabus(), teachingMaterials);
				StudentDTO student = new StudentDTO(s.getId(), s.getClass().getSimpleName(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPassword(), s.getPermissions(), s.getIndexNumber(), s.getUsername());
				TeacherDTO teacher = new TeacherDTO(n.getId(), n.getClass().getSimpleName(), n.getFirstName(), n.getLastName(), n.getEmail(), n.getPassword());
	
	
				pokusaji.add(new ExamAttemptDTO(pp.getId(), pp.getPoints(), pp.getFinalGrade(), pp.getStartTime(), pp.getEndTime(), pp.getNote(),student, course, teacher));
		}}
		return new ResponseEntity<Iterable<ExamAttemptDTO>>(pokusaji, HttpStatus.OK);
	}
	
}
