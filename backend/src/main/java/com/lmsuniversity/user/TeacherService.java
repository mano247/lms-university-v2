package com.lmsuniversity.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lmsuniversity.rectorate.University;
import com.lmsuniversity.rectorate.UniversityRepository;

@Service
public class TeacherService {
	@Autowired
	private TeacherRepository repository;

	@Autowired
	private UniversityRepository universityRepository;

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
		repository.deleteById(id);
	}

	public void delete(Teacher teacher) {
		repository.delete(teacher);
	}
}
