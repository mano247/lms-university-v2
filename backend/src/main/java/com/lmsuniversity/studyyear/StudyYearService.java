package com.lmsuniversity.studyyear;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyYearService {

	@Autowired
	private StudyYearRepository repository;

	public Iterable<StudyYear> findAll() {
		return repository.findAll();
	}

	public Optional<StudyYear> findOne(Long id) {
		return repository.findById(id);
	}

	public StudyYear save(StudyYear newStudyYear) {
		return repository.save(newStudyYear);
	}

	public StudyYear update(StudyYear studyYear) {
		if(repository.findById(studyYear.getId()).isPresent()) {
			return repository.save(studyYear);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
}
