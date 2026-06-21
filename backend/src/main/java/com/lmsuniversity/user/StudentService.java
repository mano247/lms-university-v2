package com.lmsuniversity.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.faculty.FacultyRepository;

@Service
public class StudentService {
	@Autowired
	private StudentRepository repository;

	@Autowired
	private FacultyRepository facultyRepository;

	@Autowired
	private StudentMapper mapper;

	@Autowired
	private PasswordEncoder encoder;

	public Page<StudentListDto> findAll(Pageable pageable) {
		return repository.findAllProjected(pageable);
	}

	public Optional<Student> findOne(Long id) {
		return repository.findById(id);
	}


	public Student save(Student newStudent) {
		return repository.save(newStudent);
	}

	public Student create(StudentCreateDto dto) {
		Student student = mapper.toEntity(dto);
		student.setPassword(encoder.encode(dto.getPassword()));
		if (dto.getFacultyId() != null) {
			Faculty faculty = facultyRepository.findById(dto.getFacultyId())
					.orElseThrow(() -> new IllegalArgumentException("Faculty not found: " + dto.getFacultyId()));
			student.setFaculty(faculty);
		}
		return repository.save(student);
	}

	public Student update(Long id, StudentUpdateDto dto) {
		Student student = repository.findById(id).orElse(null);
		if (student == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, student);
		return repository.save(student);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(Student student) {
		repository.delete(student);
	}
}
