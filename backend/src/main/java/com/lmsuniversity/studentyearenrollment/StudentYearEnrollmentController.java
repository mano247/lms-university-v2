package com.lmsuniversity.studentyearenrollment;

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

import com.lmsuniversity.faculty.FacultyDto;
import com.lmsuniversity.studyyear.StudyYearDto;
import com.lmsuniversity.user.StudentDto;
import com.lmsuniversity.studyprogram.StudyProgramDto;
import com.lmsuniversity.studyyear.StudyYear;
import com.lmsuniversity.studyprogram.StudyProgram;
import com.lmsuniversity.user.Student;

@Controller
@RequestMapping(path = "/api/sng")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentYearEnrollmentController {
	@Autowired
	private StudentYearEnrollmentService service;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<StudentYearEnrollmentDto>> getAll(){
		HashSet<StudentYearEnrollmentDto> enrollments = new HashSet<StudentYearEnrollmentDto>();
		for (StudentYearEnrollment s : service.findAll()) {
			StudyYear gs = s.getStudyYear();
			Student st = s.getStudent();
			StudyProgram sp = s.getStudyProgram();

			StudyYearDto studyYear = new StudyYearDto(gs.getId(),gs.getYear());
			StudentDto student = new StudentDto(st.getId(),st.getFirstName(),st.getLastName(),st.getEmail(),st.getPassword(),st.getIndexNumber(),st.getUsername());
			StudyProgramDto sProgram = new StudyProgramDto(sp.getId(),sp.getProgramCode(),sp.getDescription(),sp.getName(),sp.getProgramDirector(),new FacultyDto(sp.getFaculty().getId(),sp.getFaculty().getFacultyCode(),sp.getFaculty().getName()));


			enrollments.add(new StudentYearEnrollmentDto(s.getId(),s.getEnrollmentDate(),studyYear,student,sProgram));
		}
		return new ResponseEntity<Iterable<StudentYearEnrollmentDto>>(enrollments, HttpStatus.OK);
		}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<StudentYearEnrollmentDto> get(@PathVariable("id") Long id) {
		Optional<StudentYearEnrollment> s = service.findOne(id);
		if(s.isPresent()) {
			StudyYear gs = s.get().getStudyYear();
			Student st = s.get().getStudent();
			StudyProgram sp = s.get().getStudyProgram();

			StudyYearDto studyYear = new StudyYearDto(gs.getId(),gs.getYear());
			StudentDto student = new StudentDto(st.getId(),st.getFirstName(),st.getLastName(),st.getEmail(),st.getPassword(),st.getIndexNumber(),st.getUsername());
			StudyProgramDto sProgram = new StudyProgramDto(sp.getId(),sp.getProgramCode(),sp.getDescription(),sp.getName(),sp.getProgramDirector(),new FacultyDto(sp.getFaculty().getId(),sp.getFaculty().getFacultyCode(),sp.getFaculty().getName()));

			StudentYearEnrollmentDto dto = new StudentYearEnrollmentDto(s.get().getId(),s.get().getEnrollmentDate(),studyYear,student,sProgram);
			return new ResponseEntity<StudentYearEnrollmentDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<StudentYearEnrollmentDto>(HttpStatus.NOT_FOUND);

	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<StudentYearEnrollment> create(@RequestBody StudentYearEnrollment enrollment){
		try {
			service.save(enrollment);
			return new ResponseEntity<StudentYearEnrollment>(enrollment, HttpStatus.CREATED);
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
	public ResponseEntity<StudentYearEnrollment> update(@PathVariable("id") Long id, @RequestBody StudentYearEnrollment studentYearEnrollment){
		StudentYearEnrollment enrollment = service.findOne(id).orElse(null);

		if(enrollment != null) {
			studentYearEnrollment.setId(id);
			studentYearEnrollment = service.save(studentYearEnrollment);
			return new ResponseEntity<StudentYearEnrollment>(studentYearEnrollment, HttpStatus.OK);
				}
		return new ResponseEntity<StudentYearEnrollment>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/fbs/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<StudentYearEnrollmentDto>> getByStudentId(@PathVariable("id") Long id){
		HashSet<StudentYearEnrollmentDto> enrollments = new HashSet<StudentYearEnrollmentDto>();
		for (StudentYearEnrollment s : service.findAll()) {
			Student st = s.getStudent();
			if (id == st.getId()) {
				StudyYear gs = s.getStudyYear();
				StudyProgram sp = s.getStudyProgram();

				StudyYearDto studyYear = new StudyYearDto(gs.getId(),gs.getYear());
				StudentDto student = new StudentDto(st.getId(),st.getFirstName(),st.getLastName(),st.getEmail(),st.getPassword(),st.getIndexNumber(),st.getUsername());
				StudyProgramDto sProgram = new StudyProgramDto(sp.getId(),sp.getProgramCode(),sp.getDescription(),sp.getName(),sp.getProgramDirector(),new FacultyDto(sp.getFaculty().getId(),sp.getFaculty().getFacultyCode(),sp.getFaculty().getName()));


				enrollments.add(new StudentYearEnrollmentDto(s.getId(),s.getEnrollmentDate(),studyYear,student,sProgram));
			}
		}
		return new ResponseEntity<Iterable<StudentYearEnrollmentDto>>(enrollments, HttpStatus.OK);
		}
}


