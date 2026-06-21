package com.lmsuniversity.studentyearenrollment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
		StudentYearEnrollment enrollment = mapper.toEntity(dto);
		enrollment.setStudent(resolveStudent(dto.getStudentId()));
		enrollment.setStudyYear(resolveStudyYear(dto.getStudyYearId()));
		enrollment.setStudyProgram(resolveStudyProgram(dto.getStudyProgramId()));
		return repository.save(enrollment);
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
