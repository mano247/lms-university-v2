package rs.ac.singidunum.novisad.backend.controller;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rs.ac.singidunum.novisad.backend.dto.FacultyDTO;
import rs.ac.singidunum.novisad.backend.dto.StudyYearDTO;
import rs.ac.singidunum.novisad.backend.dto.StudentDTO;
import rs.ac.singidunum.novisad.backend.dto.StudentYearEnrollmentDTO;
import rs.ac.singidunum.novisad.backend.dto.StudyProgramDTO;
import rs.ac.singidunum.novisad.backend.model.StudyYear;
import rs.ac.singidunum.novisad.backend.model.StudentYearEnrollment;
import rs.ac.singidunum.novisad.backend.model.academic.StudyProgram;
import rs.ac.singidunum.novisad.backend.model.user.Student;
import rs.ac.singidunum.novisad.backend.service.StudentNaGodiniService;

@Controller
@RequestMapping(path = "/api/sng")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentNaGodiniController {
	@Autowired
	private StudentNaGodiniService service;
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<StudentYearEnrollmentDTO>> getAll(){
		HashSet<StudentYearEnrollmentDTO> sng = new HashSet<StudentYearEnrollmentDTO>();
		for (StudentYearEnrollment s : service.findAll()) {
			StudyYear gs = s.getStudyYear();
			Student st = s.getStudent();
			StudyProgram sp = s.getStudyProgram();
			
			StudyYearDTO studyYear = new StudyYearDTO(gs.getId(),gs.getYear());
			StudentDTO student = new StudentDTO(st.getId(),st.getFirstName(),st.getLastName(),st.getEmail(),st.getPassword(),st.getIndexNumber(),st.getUsername());
			StudyProgramDTO sProgram = new StudyProgramDTO(sp.getId(),sp.getProgramCode(),sp.getDescription(),sp.getName(),sp.getProgramDirector(),new FacultyDTO(sp.getFaculty().getId(),sp.getFaculty().getFacultyCode(),sp.getFaculty().getName()));
			
			
			sng.add(new StudentYearEnrollmentDTO(s.getId(),s.getEnrollmentDate(),studyYear,student,sProgram));
		}
		return new ResponseEntity<Iterable<StudentYearEnrollmentDTO>>(sng, HttpStatus.OK);
		}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<StudentYearEnrollmentDTO> get(@PathVariable("id") Long id) {
		Optional<StudentYearEnrollment> s = service.findOne(id);
		if(s.isPresent()) {
			StudyYear gs = s.get().getStudyYear();
			Student st = s.get().getStudent();
			StudyProgram sp = s.get().getStudyProgram();
			
			StudyYearDTO studyYear = new StudyYearDTO(gs.getId(),gs.getYear());
			StudentDTO student = new StudentDTO(st.getId(),st.getFirstName(),st.getLastName(),st.getEmail(),st.getPassword(),st.getIndexNumber(),st.getUsername());
			StudyProgramDTO sProgram = new StudyProgramDTO(sp.getId(),sp.getProgramCode(),sp.getDescription(),sp.getName(),sp.getProgramDirector(),new FacultyDTO(sp.getFaculty().getId(),sp.getFaculty().getFacultyCode(),sp.getFaculty().getName()));
			
			StudentYearEnrollmentDTO sng = new StudentYearEnrollmentDTO(s.get().getId(),s.get().getEnrollmentDate(),studyYear,student,sProgram);
			return new ResponseEntity<StudentYearEnrollmentDTO>(sng, HttpStatus.OK);
		}
		return new ResponseEntity<StudentYearEnrollmentDTO>(HttpStatus.NOT_FOUND);

	}
	
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<StudentYearEnrollment> create(@RequestBody StudentYearEnrollment sng){
		try {
			service.save(sng);
			return new ResponseEntity<StudentYearEnrollment>(sng, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<StudentYearEnrollment>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<StudentYearEnrollment> delete(@PathVariable("id") Long id){
		if(service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<StudentYearEnrollment>(HttpStatus.OK);
		}
		return new ResponseEntity<StudentYearEnrollment>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<StudentYearEnrollment> update(@PathVariable("id") Long id, @RequestBody StudentYearEnrollment studentNaGodini){
		StudentYearEnrollment sng = service.findOne(id).orElse(null);

		if(sng != null) {
			studentNaGodini.setId(id);
			studentNaGodini = service.save(studentNaGodini);
			return new ResponseEntity<StudentYearEnrollment>(studentNaGodini, HttpStatus.OK);
				}
		return new ResponseEntity<StudentYearEnrollment>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/fbs/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<StudentYearEnrollmentDTO>> getByStudentId(@PathVariable("id") Long id){
		HashSet<StudentYearEnrollmentDTO> sng = new HashSet<StudentYearEnrollmentDTO>();
		for (StudentYearEnrollment s : service.findAll()) {
			Student st = s.getStudent();
			if (id == st.getId()) {
				StudyYear gs = s.getStudyYear();
				StudyProgram sp = s.getStudyProgram();
				
				StudyYearDTO studyYear = new StudyYearDTO(gs.getId(),gs.getYear());
				StudentDTO student = new StudentDTO(st.getId(),st.getFirstName(),st.getLastName(),st.getEmail(),st.getPassword(),st.getIndexNumber(),st.getUsername());
				StudyProgramDTO sProgram = new StudyProgramDTO(sp.getId(),sp.getProgramCode(),sp.getDescription(),sp.getName(),sp.getProgramDirector(),new FacultyDTO(sp.getFaculty().getId(),sp.getFaculty().getFacultyCode(),sp.getFaculty().getName()));
				
				
				sng.add(new StudentYearEnrollmentDTO(s.getId(),s.getEnrollmentDate(),studyYear,student,sProgram));
			}
		}
		return new ResponseEntity<Iterable<StudentYearEnrollmentDTO>>(sng, HttpStatus.OK);
		}
}


