package com.lmsuniversity.examattempt;

import java.util.ArrayList;
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

import com.lmsuniversity.common.AddGradeDto;
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

	@Autowired
	private ExamAttemptMapper mapper;

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Page<ExamAttemptDto>> getAll(Pageable pageable) {
		Page<ExamAttemptDto> examAttempts = service.findAll(pageable).map(mapper::toDto);
		return new ResponseEntity<Page<ExamAttemptDto>>(examAttempts, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ExamAttemptDto> get(@PathVariable("id") Long id) {
		Optional<ExamAttempt> pp = service.findOne(id);
		if (pp.isPresent()) {
			return new ResponseEntity<ExamAttemptDto>(mapper.toDto(pp.get()), HttpStatus.OK);
		}
		return new ResponseEntity<ExamAttemptDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public ResponseEntity<ExamAttemptDto> create(@Valid @RequestBody ExamAttemptCreateDto dto, Authentication authentication){
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ExamAttempt examAttempt = service.create(dto, userDetails.getId());
		return new ResponseEntity<ExamAttemptDto>(mapper.toDto(examAttempt), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ExamAttemptDto> update(@PathVariable("id") Long id, @Valid @RequestBody ExamAttemptUpdateDto dto, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ExamAttempt examAttempt = service.update(id, dto, userDetails.getId());
		if (examAttempt != null) {
			return new ResponseEntity<ExamAttemptDto>(mapper.toDto(examAttempt), HttpStatus.OK);
		}
		return new ResponseEntity<ExamAttemptDto>(HttpStatus.NOT_FOUND);
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
				List<Long> courseIds = teacherCourses.stream().map(Course::getId).toList();
				List<ExamAttempt> courseExamAttempts = service.findByCourseIds(courseIds);

				List<AddGradeDto> response = new ArrayList<>();
				for(Course p : teacherCourses) {

					List<TeacherStudentsDto>  students = new ArrayList<TeacherStudentsDto>();
					for(Student s : p.getStudents()) {

						for(ExamAttempt pp : courseExamAttempts) {

							if(pp.getCourse().getId().equals(p.getId()) && pp.getStudent().getId().equals(s.getId())) {

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
	public ResponseEntity<List<ExamAttemptDto>> getRegisteredExamAttempts(@PathVariable("id") Long id) {
		List<ExamAttemptDto> examAttempts = service.findRegisteredByStudent(id).stream().map(mapper::toDto).collect(Collectors.toList());
		return new ResponseEntity<List<ExamAttemptDto>>(examAttempts, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/registered-by-course/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<ExamAttemptDto>> getRegisteredExamAttemptsByCourse(@PathVariable("id") Long id) {
		List<ExamAttemptDto> examAttempts = service.findRegisteredByCourse(id).stream().map(mapper::toDto).collect(Collectors.toList());
		return new ResponseEntity<List<ExamAttemptDto>>(examAttempts, HttpStatus.OK);
	}

}
