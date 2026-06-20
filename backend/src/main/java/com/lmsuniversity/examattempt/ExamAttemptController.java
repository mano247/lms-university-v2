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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
@CrossOrigin(origins = "http://localhost:4200")
public class ExamAttemptController {

	@Autowired
	private ExamAttemptService service;

	@Autowired
	private TeacherService teacherService;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Iterable<ExamAttemptDto>> getAll() {
		ArrayList<ExamAttemptDto> examAttempts = new ArrayList<ExamAttemptDto>();
		for (ExamAttempt pp : service.findAll()) {
			Course p = pp.getCourse();
			Student s = pp.getStudent();
			Teacher n = pp.getTeacher();

			Set<TeachingMaterialDto> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> new TeachingMaterialDto(
					nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
					nm.getQuantity(),
					nm.getIssuedQuantity()
			)).collect(Collectors.toSet());
			CourseDto course = new CourseDto(p.getId(),p.getCourseCode(), new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()),new StudyProgramDto(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()), p.getName(), p.getEcts(), p.getDescription(), p.getSyllabus(), teachingMaterials);
			StudentDto student = new StudentDto(s.getId(), s.getClass().getSimpleName(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPassword(), s.getPermissions(), s.getIndexNumber(), s.getUsername());
			TeacherDto teacher = new TeacherDto(n.getId(), n.getClass().getSimpleName(), n.getFirstName(), n.getLastName(), n.getEmail(), n.getPassword());


			examAttempts.add(new ExamAttemptDto(pp.getId(), pp.getPoints(), pp.getFinalGrade(), pp.getStartTime(), pp.getEndTime(), pp.getNote(),student, course, teacher));
		}
		return new ResponseEntity<Iterable<ExamAttemptDto>>(examAttempts, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ExamAttemptDto> get(@PathVariable("id") Long id) {
		Optional<ExamAttempt> pp = service.findOne(id);
		if (pp.isPresent()) {
			Course p = pp.get().getCourse();
			Student s = pp.get().getStudent();
			Teacher n = pp.get().getTeacher();
			Set<TeachingMaterialDto> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> new TeachingMaterialDto(
					nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
					nm.getQuantity(),
					nm.getIssuedQuantity()
			)).collect(Collectors.toSet());
			CourseDto course = new CourseDto(p.getId(),p.getCourseCode(),  new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()), new StudyProgramDto(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()), p.getName(), p.getEcts(), p.getDescription(), p.getSyllabus(), teachingMaterials);
			StudentDto student = new StudentDto(s.getId(), s.getClass().getSimpleName(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPassword(), s.getPermissions(), s.getIndexNumber(), s.getUsername());
			TeacherDto teacher = new TeacherDto(n.getId(), n.getClass().getSimpleName(), n.getFirstName(), n.getLastName(), n.getEmail(), n.getPassword());

			ExamAttemptDto examAttempt = new ExamAttemptDto(pp.get().getId(), pp.get().getPoints(), pp.get().getFinalGrade(),pp.get().getStartTime(),pp.get().getEndTime(),pp.get().getNote(),student, course,  teacher);
			return new ResponseEntity<ExamAttemptDto>(examAttempt, HttpStatus.OK);
		}
		return new ResponseEntity<ExamAttemptDto>(HttpStatus.NOT_FOUND);
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
	public ResponseEntity<ExamAttempt> update(@PathVariable("id") Long id, @RequestBody ExamAttempt examAttempt) {

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
	@RequestMapping(path = "/dobaviNastavniku/{idNastavnika}", method = RequestMethod.GET)
	public ResponseEntity<List<AddGradeDto>> getTeacherCourses(
			@PathVariable("idNastavnika") Long teacherId,
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

								TeacherStudentsDto student = new TeacherStudentsDto(s.getId(), pp.getId(), s.getFirstName(), s.getLastName(), s.getIndexNumber(), pp.getPoints(), pp.getFinalGrade());
								students.add(student);
								break;

							}
						}


					}
					if(students.size() > 0 ) {
						response.add(new AddGradeDto(
								p.getId(), p.getName(), p.getEcts(), p.getSyllabus(), students));
					}
				}
				return new ResponseEntity<List<AddGradeDto>>(response, HttpStatus.OK);

			}
		}

		return new ResponseEntity<List<AddGradeDto>>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "prijavljeni/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<ExamAttemptDto>> getRegisteredExamAttempts(@PathVariable("id") Long id) {
		ArrayList<ExamAttemptDto> examAttempts = new ArrayList<ExamAttemptDto>();
		for (ExamAttempt pp : service.findAll()) {
			if(pp.getFinalGrade()==0 && pp.getPoints()==0.0 && pp.getStudent().getId()==id) {
				Course p = pp.getCourse();
				Student s = pp.getStudent();
				Teacher n = pp.getTeacher();

				Set<TeachingMaterialDto> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> new TeachingMaterialDto(
						nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
						nm.getQuantity(),
						nm.getIssuedQuantity()
				)).collect(Collectors.toSet());
				CourseDto course = new CourseDto(p.getId(),p.getCourseCode(),  new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()), new StudyProgramDto(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()), p.getName(), p.getEcts(), p.getDescription(), p.getSyllabus(), teachingMaterials);
				StudentDto student = new StudentDto(s.getId(), s.getClass().getSimpleName(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPassword(), s.getPermissions(), s.getIndexNumber(), s.getUsername());
				TeacherDto teacher = new TeacherDto(n.getId(), n.getClass().getSimpleName(), n.getFirstName(), n.getLastName(), n.getEmail(), n.getPassword());


				examAttempts.add(new ExamAttemptDto(pp.getId(), pp.getPoints(), pp.getFinalGrade(), pp.getStartTime(), pp.getEndTime(), pp.getNote(),student, course, teacher));
		}}
		return new ResponseEntity<Iterable<ExamAttemptDto>>(examAttempts, HttpStatus.OK);
	}

	@RequestMapping(path = "prijavljeniPoPredmetu/{id}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<ExamAttemptDto>> getRegisteredExamAttemptsByCourse(@PathVariable("id") Long id) {
		ArrayList<ExamAttemptDto> examAttempts = new ArrayList<ExamAttemptDto>();
		for (ExamAttempt pp : service.findAll()) {
			if(pp.getFinalGrade()==0 && pp.getPoints()==0.0 && pp.getCourse().getId().equals(id)) {
				Course p = pp.getCourse();
				Student s = pp.getStudent();
				Teacher n = pp.getTeacher();

				Set<TeachingMaterialDto> teachingMaterials = p.getTeachingMaterials().stream().map(nm -> new TeachingMaterialDto(
						nm.getId(), nm.getTitle(), nm.getAuthors(), nm.getPageCount(), nm.getPublisher(), nm.getDescription(),
						nm.getQuantity(),
						nm.getIssuedQuantity()
				)).collect(Collectors.toSet());
				CourseDto course = new CourseDto(p.getId(),p.getCourseCode(),  new TeacherDto(p.getTeacher().getId(),p.getTeacher().getFirstName(),p.getTeacher().getLastName()), new StudyProgramDto(p.getStudyProgram().getId(),p.getStudyProgram().getProgramCode(),p.getStudyProgram().getName()), p.getName(), p.getEcts(), p.getDescription(), p.getSyllabus(), teachingMaterials);
				StudentDto student = new StudentDto(s.getId(), s.getClass().getSimpleName(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPassword(), s.getPermissions(), s.getIndexNumber(), s.getUsername());
				TeacherDto teacher = new TeacherDto(n.getId(), n.getClass().getSimpleName(), n.getFirstName(), n.getLastName(), n.getEmail(), n.getPassword());


				examAttempts.add(new ExamAttemptDto(pp.getId(), pp.getPoints(), pp.getFinalGrade(), pp.getStartTime(), pp.getEndTime(), pp.getNote(),student, course, teacher));
		}}
		return new ResponseEntity<Iterable<ExamAttemptDto>>(examAttempts, HttpStatus.OK);
	}

}
