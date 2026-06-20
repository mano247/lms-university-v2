package com.lmsuniversity.rectorate;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversityService {
	@Autowired
	private UniversityRepository repository;

	public Iterable<University> findAll() {
		return repository.findAll();
	}

	public Optional<University> findOne(Long id) {
		return repository.findById(id);
	}


	public University save(University newUniversity) {
		return repository.save(newUniversity);
	}

	public University update(University university) {
		if(repository.findById(university.getId()).isPresent()) {
			return repository.save(university);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(University university) {
		repository.delete(university);
	}

}
