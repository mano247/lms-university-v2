package com.lmsuniversity.studyprogram;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lmsuniversity.course.CourseRepository;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.faculty.FacultyRepository;
import com.lmsuniversity.studentyearenrollment.StudentYearEnrollmentRepository;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.TeacherRepository;

@Service
public class StudyProgramService {
	@Autowired
	private StudyProgramRepository repository;

	@Autowired
	private FacultyRepository facultyRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentYearEnrollmentRepository studentYearEnrollmentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private StudyProgramMapper mapper;

	public List<StudyProgram> findAll() {
		return repository.findAll();
	}

	public Optional<StudyProgram> findOne(Long id) {
		return repository.findById(id);
	}

	public Optional<StudyProgram> findByProgramCode(String programCode) {
		return repository.findByProgramCode(programCode);
	}

	public StudyProgram save(StudyProgram newStudyProgram) {
		return repository.save(newStudyProgram);
	}

	public StudyProgram create(StudyProgramCreateDto dto) {
		Faculty faculty = facultyRepository.findById(dto.getFacultyId())
				.orElseThrow(() -> new IllegalArgumentException("Faculty not found: " + dto.getFacultyId()));
		StudyProgram studyProgram = mapper.toEntity(dto);
		studyProgram.setFaculty(faculty);
		studyProgram.setProgramDirector(resolveProgramDirector(dto.getProgramDirectorId()));
		return repository.save(studyProgram);
	}

	public StudyProgram update(Long id, StudyProgramUpdateDto dto) {
		StudyProgram studyProgram = repository.findById(id).orElse(null);
		if (studyProgram == null) {
			return null;
		}
		Faculty faculty = facultyRepository.findById(dto.getFacultyId())
				.orElseThrow(() -> new IllegalArgumentException("Faculty not found: " + dto.getFacultyId()));
		mapper.updateEntityFromDto(dto, studyProgram);
		studyProgram.setFaculty(faculty);
		studyProgram.setProgramDirector(resolveProgramDirector(dto.getProgramDirectorId()));
		return repository.save(studyProgram);
	}

	private Teacher resolveProgramDirector(Long programDirectorId) {
		if (programDirectorId == null) {
			return null;
		}
		return teacherRepository.findById(programDirectorId)
				.orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + programDirectorId));
	}

	public void delete(Long id) {
		if (courseRepository.existsByStudyProgramId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete study program: it still has courses associated with it.");
		}
		if (studentYearEnrollmentRepository.existsByStudyProgramId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete study program: it still has student year enrollments associated with it.");
		}
		repository.deleteById(id);
	}

	public void delete(StudyProgram studyProgram) {
		repository.delete(studyProgram);
	}

}
