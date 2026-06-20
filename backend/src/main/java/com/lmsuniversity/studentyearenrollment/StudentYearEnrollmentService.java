package com.lmsuniversity.studentyearenrollment;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentYearEnrollmentService {

	@Autowired
	private StudentYearEnrollmentRepository repository;

	public Iterable<StudentYearEnrollment> findAll() {
		return repository.findAll();
	}

	public Optional<StudentYearEnrollment> findOne(Long id) {
		return repository.findById(id);
	}


	public StudentYearEnrollment save(StudentYearEnrollment newStudentYearEnrollment) {
		return repository.save(newStudentYearEnrollment);
	}

	public StudentYearEnrollment update(StudentYearEnrollment studentYearEnrollment) {
		if(repository.findById(studentYearEnrollment.getId()).isPresent()) {
			return repository.save(studentYearEnrollment);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(StudentYearEnrollment studentYearEnrollment) {
		repository.delete(studentYearEnrollment);
	}

}
