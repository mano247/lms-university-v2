package com.lmsuniversity.studentyearenrollment;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lmsuniversity.course.Course;
import com.lmsuniversity.course.CourseRepository;
import com.lmsuniversity.studyprogram.StudyProgram;
import com.lmsuniversity.studyprogram.StudyProgramRepository;
import com.lmsuniversity.studyyear.StudyYear;
import com.lmsuniversity.studyyear.StudyYearRepository;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.user.StudentRepository;

@Service
public class StudentYearEnrollmentService {

	@Autowired
	private StudentYearEnrollmentRepository repository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudyYearRepository studyYearRepository;

	@Autowired
	private StudyProgramRepository studyProgramRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentYearEnrollmentMapper mapper;

	public Page<StudentYearEnrollment> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Optional<StudentYearEnrollment> findOne(Long id) {
		return repository.findById(id);
	}

	public List<StudentYearEnrollment> findByStudentId(Long studentId) {
		return repository.findByStudentId(studentId);
	}


	public StudentYearEnrollment save(StudentYearEnrollment newStudentYearEnrollment) {
		return repository.save(newStudentYearEnrollment);
	}

	public StudentYearEnrollment create(StudentYearEnrollmentCreateDto dto) {
		Student student = resolveStudent(dto.getStudentId());
		return enroll(student, dto.getStudyProgramId(), dto.getStudyYearId(), dto.getEnrollmentDate());
	}

	public StudentYearEnrollment update(Long id, StudentYearEnrollmentUpdateDto dto) {
		StudentYearEnrollment enrollment = repository.findById(id).orElse(null);
		if (enrollment == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, enrollment);
		enrollment.setStudent(resolveStudent(dto.getStudentId()));
		enrollment.setStudyYear(resolveStudyYear(dto.getStudyYearId()));
		enrollment.setStudyProgram(resolveStudyProgram(dto.getStudyProgramId()));
		return repository.save(enrollment);
	}

	/**
	 * Creates a {@link StudentYearEnrollment} for the given student and replaces their
	 * current course set with exactly the courses of the target study program + study year.
	 * Shared by the "enroll new student" flow (RegisteredUserService) and the
	 * "enroll existing student into next year" flow (this controller's create()).
	 */
	public StudentYearEnrollment enroll(Student student, Long studyProgramId, Long studyYearId, Date enrollmentDate) {
		StudyYear studyYear = resolveStudyYear(studyYearId);
		StudyProgram studyProgram = resolveStudyProgram(studyProgramId);

		StudentYearEnrollment enrollment = StudentYearEnrollment.builder()
				.student(student)
				.studyYear(studyYear)
				.studyProgram(studyProgram)
				.enrollmentDate(enrollmentDate)
				.build();
		StudentYearEnrollment saved = repository.save(enrollment);

		List<Course> courses = courseRepository.findByStudyProgramIdAndStudyYearId(studyProgramId, studyYearId);
		student.setCourses(new HashSet<>(courses));
		studentRepository.save(student);

		return saved;
	}

	private Student resolveStudent(Long id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
	}

	private StudyYear resolveStudyYear(Long id) {
		return studyYearRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Study year not found: " + id));
	}

	private StudyProgram resolveStudyProgram(Long id) {
		return studyProgramRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Study program not found: " + id));
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(StudentYearEnrollment studentYearEnrollment) {
		repository.delete(studentYearEnrollment);
	}

}
