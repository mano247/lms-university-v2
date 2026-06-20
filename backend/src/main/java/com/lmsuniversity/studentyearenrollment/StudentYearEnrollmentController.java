package com.lmsuniversity.studentyearenrollment;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
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
@RequestMapping(path = "/api/enrollments")
@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
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

			StudyYearDto studyYear = StudyYearDto.builder().id(gs.getId()).year(gs.getYear()).build();
			StudentDto student = StudentDto.builder().id(st.getId()).firstName(st.getFirstName()).lastName(st.getLastName()).email(st.getEmail()).password(st.getPassword()).indexNumber(st.getIndexNumber()).username(st.getUsername()).build();
			StudyProgramDto sProgram = StudyProgramDto.builder().id(sp.getId()).programCode(sp.getProgramCode()).description(sp.getDescription()).name(sp.getName()).programDirector(sp.getProgramDirector()).faculty(FacultyDto.builder().id(sp.getFaculty().getId()).facultyCode(sp.getFaculty().getFacultyCode()).name(sp.getFaculty().getName()).build()).build();


			enrollments.add(StudentYearEnrollmentDto.builder().id(s.getId()).enrollmentDate(s.getEnrollmentDate()).studyYear(studyYear).student(student).studyProgram(sProgram).build());
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

			StudyYearDto studyYear = StudyYearDto.builder().id(gs.getId()).year(gs.getYear()).build();
			StudentDto student = StudentDto.builder().id(st.getId()).firstName(st.getFirstName()).lastName(st.getLastName()).email(st.getEmail()).password(st.getPassword()).indexNumber(st.getIndexNumber()).username(st.getUsername()).build();
			StudyProgramDto sProgram = StudyProgramDto.builder().id(sp.getId()).programCode(sp.getProgramCode()).description(sp.getDescription()).name(sp.getName()).programDirector(sp.getProgramDirector()).faculty(FacultyDto.builder().id(sp.getFaculty().getId()).facultyCode(sp.getFaculty().getFacultyCode()).name(sp.getFaculty().getName()).build()).build();

			StudentYearEnrollmentDto dto = StudentYearEnrollmentDto.builder().id(s.get().getId()).enrollmentDate(s.get().getEnrollmentDate()).studyYear(studyYear).student(student).studyProgram(sProgram).build();
			return new ResponseEntity<StudentYearEnrollmentDto>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<StudentYearEnrollmentDto>(HttpStatus.NOT_FOUND);

	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<StudentYearEnrollment> create(@Valid @RequestBody StudentYearEnrollment enrollment){
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
	public ResponseEntity<StudentYearEnrollment> update(@PathVariable("id") Long id, @Valid @RequestBody StudentYearEnrollment studentYearEnrollment){
		StudentYearEnrollment enrollment = service.findOne(id).orElse(null);

		if(enrollment != null) {
			studentYearEnrollment.setId(id);
			studentYearEnrollment = service.save(studentYearEnrollment);
			return new ResponseEntity<StudentYearEnrollment>(studentYearEnrollment, HttpStatus.OK);
				}
		return new ResponseEntity<StudentYearEnrollment>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "/by-student/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<StudentYearEnrollmentDto>> getByStudentId(@PathVariable("id") Long id){
		HashSet<StudentYearEnrollmentDto> enrollments = new HashSet<StudentYearEnrollmentDto>();
		for (StudentYearEnrollment s : service.findAll()) {
			Student st = s.getStudent();
			if (id == st.getId()) {
				StudyYear gs = s.getStudyYear();
				StudyProgram sp = s.getStudyProgram();

				StudyYearDto studyYear = StudyYearDto.builder().id(gs.getId()).year(gs.getYear()).build();
				StudentDto student = StudentDto.builder().id(st.getId()).firstName(st.getFirstName()).lastName(st.getLastName()).email(st.getEmail()).password(st.getPassword()).indexNumber(st.getIndexNumber()).username(st.getUsername()).build();
				StudyProgramDto sProgram = StudyProgramDto.builder().id(sp.getId()).programCode(sp.getProgramCode()).description(sp.getDescription()).name(sp.getName()).programDirector(sp.getProgramDirector()).faculty(FacultyDto.builder().id(sp.getFaculty().getId()).facultyCode(sp.getFaculty().getFacultyCode()).name(sp.getFaculty().getName()).build()).build();


				enrollments.add(StudentYearEnrollmentDto.builder().id(s.getId()).enrollmentDate(s.getEnrollmentDate()).studyYear(studyYear).student(student).studyProgram(sProgram).build());
			}
		}
		return new ResponseEntity<Iterable<StudentYearEnrollmentDto>>(enrollments, HttpStatus.OK);
		}
}


