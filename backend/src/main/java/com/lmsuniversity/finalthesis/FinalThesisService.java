package com.lmsuniversity.finalthesis;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lmsuniversity.user.Student;
import com.lmsuniversity.user.StudentRepository;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.TeacherRepository;

@Service
public class FinalThesisService {
	@Autowired
	private FinalThesisRepository repository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private FinalThesisMapper mapper;

	public Page<FinalThesis> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Optional<FinalThesis> findOne(Long id) {
		return repository.findById(id);
	}

	public Optional<FinalThesis> findByStudentId(Long studentId) {
		return repository.findByStudentId(studentId);
	}

	public List<FinalThesis> findByMentorId(Long mentorId) {
		return repository.findByMentorId(mentorId);
	}


	public FinalThesis save(FinalThesis newFinalThesis) {
		return repository.save(newFinalThesis);
	}

	public boolean existsByStudentId(Long studentId) {
		return repository.existsByStudentId(studentId);
	}

	public FinalThesis create(FinalThesisCreateDto dto) {
		Student student = studentRepository.findById(dto.getStudentId())
				.orElseThrow(() -> new IllegalArgumentException("Student not found: " + dto.getStudentId()));
		Teacher mentor = teacherRepository.findById(dto.getMentorId())
				.orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + dto.getMentorId()));
		FinalThesis finalThesis = mapper.toEntity(dto);
		finalThesis.setStudent(student);
		finalThesis.setMentor(mentor);
		return repository.save(finalThesis);
	}

	public FinalThesis update(Long id, FinalThesisUpdateDto dto) {
		FinalThesis finalThesis = repository.findById(id).orElse(null);
		if (finalThesis == null) {
			return null;
		}
		Student student = studentRepository.findById(dto.getStudentId())
				.orElseThrow(() -> new IllegalArgumentException("Student not found: " + dto.getStudentId()));
		Teacher mentor = teacherRepository.findById(dto.getMentorId())
				.orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + dto.getMentorId()));
		mapper.updateEntityFromDto(dto, finalThesis);
		finalThesis.setStudent(student);
		finalThesis.setMentor(mentor);
		return repository.save(finalThesis);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(FinalThesis finalThesis) {
		repository.delete(finalThesis);
	}
}
