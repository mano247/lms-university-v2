package com.lmsuniversity.announcement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CourseAnnouncementService {
	@Autowired
	private CourseAnnouncementRepository repository;

	public Iterable<CourseAnnouncement> findAll() {
		return repository.findAll();
	}

	public Optional<CourseAnnouncement> findOne(Long id) {
		return repository.findById(id);
	}


	public CourseAnnouncement save(CourseAnnouncement newCourseAnnouncement) {
		return repository.save(newCourseAnnouncement);
	}

	public CourseAnnouncement update(CourseAnnouncement courseAnnouncement) {
		if(repository.findById(courseAnnouncement.getId()).isPresent()) {
			return repository.save(courseAnnouncement);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(CourseAnnouncement courseAnnouncement) {
		repository.delete(courseAnnouncement);
	}
}
