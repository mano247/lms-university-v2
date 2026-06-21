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
public class StudentAffairsOfficeService {
	@Autowired
	private StudentAffairsOfficeRepository repository;

	@Autowired
	private FacultyRepository facultyRepository;

	@Autowired
	private StudentAffairsOfficeMapper mapper;

	@Autowired
	private PasswordEncoder encoder;

	public Page<StudentAffairsOffice> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Optional<StudentAffairsOffice> findOne(Long id) {
		return repository.findById(id);
	}


	public StudentAffairsOffice save(StudentAffairsOffice newStudentAffairsOffice) {
		return repository.save(newStudentAffairsOffice);
	}

	public StudentAffairsOffice create(StudentAffairsOfficeCreateDto dto) {
		StudentAffairsOffice studentAffairsOffice = mapper.toEntity(dto);
		studentAffairsOffice.setPassword(encoder.encode(dto.getPassword()));
		if (dto.getFacultyId() != null) {
			Faculty faculty = facultyRepository.findById(dto.getFacultyId())
					.orElseThrow(() -> new IllegalArgumentException("Faculty not found: " + dto.getFacultyId()));
			studentAffairsOffice.setFaculty(faculty);
		}
		return repository.save(studentAffairsOffice);
	}

	public StudentAffairsOffice update(Long id, StudentAffairsOfficeUpdateDto dto) {
		StudentAffairsOffice studentAffairsOffice = repository.findById(id).orElse(null);
		if (studentAffairsOffice == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, studentAffairsOffice);
		if (dto.getFacultyId() != null) {
			Faculty faculty = facultyRepository.findById(dto.getFacultyId())
					.orElseThrow(() -> new IllegalArgumentException("Faculty not found: " + dto.getFacultyId()));
			studentAffairsOffice.setFaculty(faculty);
		} else {
			studentAffairsOffice.setFaculty(null);
		}
		return repository.save(studentAffairsOffice);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(StudentAffairsOffice studentAffairsOffice) {
		repository.delete(studentAffairsOffice);
	}
}
