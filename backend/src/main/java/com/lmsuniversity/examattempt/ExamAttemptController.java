package com.lmsuniversity.examattempt;

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
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lmsuniversity.common.AddGradeDto;
import com.lmsuniversity.teachingmaterial.TeachingMaterialDto;
import com.lmsuniversity.user.TeacherDto;
import com.lmsuniversity.course.CourseDto;
import com.lmsuniversity.user.StudentDto;
import com.lmsuniversity.studyprogram.StudyProgramDto;
import com.lmsuniversity.common.TeacherStudentsDto;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.security.services.UserDetailsImpl;
import com.lmsuniversity.user.TeacherService;

@Controller
@RequestMapping(path = "/api/examAttempts")
public class ExamAttemptController {

	@Autowired
	private ExamAttemptService service;

	@Autowired
	private TeacherService teacherService;

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<ExamAttemptDto>> getAll() {
		ArrayList<ExamAttemptDto> examAttempts = new ArrayList<ExamAttemptDto>();
		for (ExamAttempt pp : service.findAll()) {
			Course p = pp.getCourse();
			Student s = pp.getStudent();
			Teacher n = pp.getTeacher();

			Set<TeachingMaterialDto> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet());
			CourseDto course = CourseDto.builder().id(p.getId()).courseCode(p.getCourseCode()).teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build()).studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build()).name(p.getName()).ects(p.getEcts()).description(p.getDescription()).syllabus(p.getSyllabus()).teachingMaterials(teachingMaterials).build();
			StudentDto student = StudentDto.builder().id(s.getId()).userType(s.getClass().getSimpleName()).firstName(s.getFirstName()).lastName(s.getLastName()).email(s.getEmail()).password(s.getPassword()).permission(s.getPermissions()).indexNumber(s.getIndexNumber()).username(s.getUsername()).build();
			TeacherDto teacher = TeacherDto.builder().id(n.getId()).userType(n.getClass().getSimpleName()).firstName(n.getFirstName()).lastName(n.getLastName()).email(n.getEmail()).build();


			examAttempts.add(ExamAttemptDto.builder().id(pp.getId()).points(pp.getPoints()).finalGrade(pp.getFinalGrade()).startTime(pp.getStartTime()).endTime(pp.getEndTime()).note(pp.getNote()).student(student).course(course).teacher(teacher).build());
		}
		return new ResponseEntity<Iterable<ExamAttemptDto>>(examAttempts, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ExamAttemptDto> get(@PathVariable("id") Long id) {
		Optional<ExamAttempt> pp = service.findOne(id);
		if (pp.isPresent()) {
			Course p = pp.get().getCourse();
			Student s = pp.get().getStudent();
			Teacher n = pp.get().getTeacher();
			Set<TeachingMaterialDto> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet());
			CourseDto course = CourseDto.builder().id(p.getId()).courseCode(p.getCourseCode()).teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build()).studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build()).name(p.getName()).ects(p.getEcts()).description(p.getDescription()).syllabus(p.getSyllabus()).teachingMaterials(teachingMaterials).build();
			StudentDto student = StudentDto.builder().id(s.getId()).userType(s.getClass().getSimpleName()).firstName(s.getFirstName()).lastName(s.getLastName()).email(s.getEmail()).password(s.getPassword()).permission(s.getPermissions()).indexNumber(s.getIndexNumber()).username(s.getUsername()).build();
			TeacherDto teacher = TeacherDto.builder().id(n.getId()).userType(n.getClass().getSimpleName()).firstName(n.getFirstName()).lastName(n.getLastName()).email(n.getEmail()).build();

			ExamAttemptDto examAttempt = ExamAttemptDto.builder().id(pp.get().getId()).points(pp.get().getPoints()).finalGrade(pp.get().getFinalGrade()).startTime(pp.get().getStartTime()).endTime(pp.get().getEndTime()).note(pp.get().getNote()).student(student).course(course).teacher(teacher).build();
			return new ResponseEntity<ExamAttemptDto>(examAttempt, HttpStatus.OK);
		}
		return new ResponseEntity<ExamAttemptDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public ResponseEntity<ExamAttempt> create(@Valid @RequestBody ExamAttempt p){
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
	public ResponseEntity<ExamAttempt> update(@PathVariable("id") Long id, @Valid @RequestBody ExamAttempt examAttempt) {

		ExamAttempt u = service.findOne(id).orElse(null);
		if (u != null) {

			examAttempt.setId(id);
			if (examAttempt.getPoints() == 0) {
				examAttempt.setPoints(u.getPoints());
			}
			if(examAttempt.getFinalGrade() == 0) {
				examAttempt.setFinalGrade(u.getFinalGrade());
			}
			examAttempt.setCourse(u.getCourse());
			examAttempt.setStudent(u.getStudent());
			examAttempt.setTeacher(u.getTeacher());
			service.save(examAttempt);

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
	@RequestMapping(path = "/by-teacher/{teacherId}", method = RequestMethod.GET)
	public ResponseEntity<List<AddGradeDto>> getTeacherCourses(
			@PathVariable("teacherId") Long teacherId,
			Authentication authentication
	) {
		if(authentication.isAuthenticated()) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Long userId = userDetails.getId();

			if (teacherId.equals(userId)) {

				Set<Course> teacherCourses = teacherService.findOne(teacherId).get().getCourses();

				List<AddGradeDto> response = new ArrayList<>();
				for(Course p : teacherCourses) {

					List<TeacherStudentsDto>  students = new ArrayList<TeacherStudentsDto>();
					for(Student s : p.getStudents()) {

						for(ExamAttempt pp : service.findAll()) {

							if(pp.getCourse().getId().equals(p.getId()) && pp.getStudent().getId() == s.getId()) {

								TeacherStudentsDto student = TeacherStudentsDto.builder().studentId(s.getId()).examAttemptId(pp.getId()).studentFirstName(s.getFirstName()).studentLastName(s.getLastName()).indexNumber(s.getIndexNumber()).points(pp.getPoints()).grade(pp.getFinalGrade()).build();
								students.add(student);
								break;

							}
						}


					}
					if(students.size() > 0 ) {
						response.add(AddGradeDto.builder()
								.courseId(p.getId())
								.courseName(p.getName())
								.ects(p.getEcts())
								.syllabus(p.getSyllabus())
								.studentsInCourse(students)
								.build());
					}
				}
				return new ResponseEntity<List<AddGradeDto>>(response, HttpStatus.OK);

			}
		}

		return new ResponseEntity<List<AddGradeDto>>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/registered/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<ExamAttemptDto>> getRegisteredExamAttempts(@PathVariable("id") Long id) {
		ArrayList<ExamAttemptDto> examAttempts = new ArrayList<ExamAttemptDto>();
		for (ExamAttempt pp : service.findAll()) {
			if(pp.getFinalGrade()==0 && pp.getPoints()==0.0 && pp.getStudent().getId()==id) {
				Course p = pp.getCourse();
				Student s = pp.getStudent();
				Teacher n = pp.getTeacher();

				Set<TeachingMaterialDto> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet());
				CourseDto course = CourseDto.builder().id(p.getId()).courseCode(p.getCourseCode()).teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build()).studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build()).name(p.getName()).ects(p.getEcts()).description(p.getDescription()).syllabus(p.getSyllabus()).teachingMaterials(teachingMaterials).build();
				StudentDto student = StudentDto.builder().id(s.getId()).userType(s.getClass().getSimpleName()).firstName(s.getFirstName()).lastName(s.getLastName()).email(s.getEmail()).password(s.getPassword()).permission(s.getPermissions()).indexNumber(s.getIndexNumber()).username(s.getUsername()).build();
				TeacherDto teacher = TeacherDto.builder().id(n.getId()).userType(n.getClass().getSimpleName()).firstName(n.getFirstName()).lastName(n.getLastName()).email(n.getEmail()).build();


				examAttempts.add(ExamAttemptDto.builder().id(pp.getId()).points(pp.getPoints()).finalGrade(pp.getFinalGrade()).startTime(pp.getStartTime()).endTime(pp.getEndTime()).note(pp.getNote()).student(student).course(course).teacher(teacher).build());
		}}
		return new ResponseEntity<Iterable<ExamAttemptDto>>(examAttempts, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/registered-by-course/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<ExamAttemptDto>> getRegisteredExamAttemptsByCourse(@PathVariable("id") Long id) {
		ArrayList<ExamAttemptDto> examAttempts = new ArrayList<ExamAttemptDto>();
		for (ExamAttempt pp : service.findAll()) {
			if(pp.getFinalGrade()==0 && pp.getPoints()==0.0 && pp.getCourse().getId().equals(id)) {
				Course p = pp.getCourse();
				Student s = pp.getStudent();
				Teacher n = pp.getTeacher();

				Set<TeachingMaterialDto> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> TeachingMaterialDto.builder().id(nm.getId()).title(nm.getTitle()).authors(nm.getAuthors()).pageCount(nm.getPageCount()).publisher(nm.getPublisher()).description(nm.getDescription()).quantity(nm.getQuantity()).issuedQuantity(nm.getIssuedQuantity()).build()).collect(Collectors.toSet());
				CourseDto course = CourseDto.builder().id(p.getId()).courseCode(p.getCourseCode()).teacher(TeacherDto.builder().id(p.getTeacher().getId()).firstName(p.getTeacher().getFirstName()).lastName(p.getTeacher().getLastName()).build()).studyProgram(StudyProgramDto.builder().id(p.getStudyProgram().getId()).programCode(p.getStudyProgram().getProgramCode()).name(p.getStudyProgram().getName()).build()).name(p.getName()).ects(p.getEcts()).description(p.getDescription()).syllabus(p.getSyllabus()).teachingMaterials(teachingMaterials).build();
				StudentDto student = StudentDto.builder().id(s.getId()).userType(s.getClass().getSimpleName()).firstName(s.getFirstName()).lastName(s.getLastName()).email(s.getEmail()).password(s.getPassword()).permission(s.getPermissions()).indexNumber(s.getIndexNumber()).username(s.getUsername()).build();
				TeacherDto teacher = TeacherDto.builder().id(n.getId()).userType(n.getClass().getSimpleName()).firstName(n.getFirstName()).lastName(n.getLastName()).email(n.getEmail()).build();


				examAttempts.add(ExamAttemptDto.builder().id(pp.getId()).points(pp.getPoints()).finalGrade(pp.getFinalGrade()).startTime(pp.getStartTime()).endTime(pp.getEndTime()).note(pp.getNote()).student(student).course(course).teacher(teacher).build());
		}}
		return new ResponseEntity<Iterable<ExamAttemptDto>>(examAttempts, HttpStatus.OK);
	}

}
