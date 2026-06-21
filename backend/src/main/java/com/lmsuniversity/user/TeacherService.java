package com.lmsuniversity.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lmsuniversity.course.CourseRepository;
import com.lmsuniversity.examattempt.ExamAttemptRepository;
import com.lmsuniversity.finalthesis.FinalThesisRepository;
import com.lmsuniversity.rectorate.University;
import com.lmsuniversity.rectorate.UniversityRepository;

@Service
public class TeacherService {
	@Autowired
	private TeacherRepository repository;

	@Autowired
	private UniversityRepository universityRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private ExamAttemptRepository examAttemptRepository;

	@Autowired
	private FinalThesisRepository finalThesisRepository;

	@Autowired
	private TeacherMapper mapper;

	@Autowired
	private PasswordEncoder encoder;

	public Page<Teacher> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Optional<Teacher> findOne(Long id) {
		return repository.findById(id);
	}


	public Teacher save(Teacher newTeacher) {
		return repository.save(newTeacher);
	}

	public Teacher create(TeacherCreateDto dto) {
		Teacher teacher = mapper.toEntity(dto);
		teacher.setPassword(encoder.encode(dto.getPassword()));
		if (dto.getUniversityId() != null) {
			University university = universityRepository.findById(dto.getUniversityId())
					.orElseThrow(() -> new IllegalArgumentException("University not found: " + dto.getUniversityId()));
			teacher.setUniversity(university);
		}
		return repository.save(teacher);
	}

	public Teacher update(Long id, TeacherUpdateDto dto) {
		Teacher teacher = repository.findById(id).orElse(null);
		if (teacher == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, teacher);
		if (dto.getUniversityId() != null) {
			University university = universityRepository.findById(dto.getUniversityId())
					.orElseThrow(() -> new IllegalArgumentException("University not found: " + dto.getUniversityId()));
			teacher.setUniversity(university);
		} else {
			teacher.setUniversity(null);
		}
		return repository.save(teacher);
	}

	public void delete(Long id) {
		if (courseRepository.existsByTeacherId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete teacher: they still teach courses.");
		}
		if (examAttemptRepository.existsByTeacherId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete teacher: they still have exam attempts associated with them.");
		}
		if (finalThesisRepository.existsByMentorId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete teacher: they still mentor final theses.");
		}
		repository.deleteById(id);
	}

	public void delete(Teacher teacher) {
		repository.delete(teacher);
	}
}
