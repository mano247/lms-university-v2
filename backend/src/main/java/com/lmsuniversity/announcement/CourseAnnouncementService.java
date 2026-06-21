package com.lmsuniversity.announcement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsuniversity.course.Course;
import com.lmsuniversity.course.CourseRepository;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.user.StudentAffairsOffice;
import com.lmsuniversity.user.StudentAffairsOfficeRepository;

@Service
public class CourseAnnouncementService {
	@Autowired
	private CourseAnnouncementRepository repository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentAffairsOfficeRepository studentAffairsOfficeRepository;

	@Autowired
	private CourseAnnouncementMapper mapper;

	public List<CourseAnnouncement> findAll() {
		return repository.findAll();
	}

	public Optional<CourseAnnouncement> findOne(Long id) {
		return repository.findById(id);
	}


	public CourseAnnouncement save(CourseAnnouncement newCourseAnnouncement) {
		return repository.save(newCourseAnnouncement);
	}

	public CourseAnnouncement create(CourseAnnouncementCreateDto dto, Long requestingTeacherId) {
		Course course = courseRepository.findById(dto.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException("Course not found: " + dto.getCourseId()));
		if (!course.getTeacher().getId().equals(requestingTeacherId)) {
			return null;
		}

		CourseAnnouncement courseAnnouncement = mapper.toEntity(dto);
		courseAnnouncement.setCourse(course);
		courseAnnouncement.setImage(dto.getImage() != null ? dto.getImage() : "");
		courseAnnouncement.setDate(LocalDateTime.now());
		return repository.save(courseAnnouncement);
	}

	public CourseAnnouncement update(Long id, CourseAnnouncementUpdateDto dto, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		CourseAnnouncement courseAnnouncement = repository.findById(id).orElse(null);
		if (courseAnnouncement == null || !canManage(courseAnnouncement.getCourse(), requestingUserId, isAdmin, isStudentAffairs)) {
			return null;
		}
		mapper.updateEntityFromDto(dto, courseAnnouncement);
		if (dto.getImage() != null && !dto.getImage().isBlank()) {
			courseAnnouncement.setImage(dto.getImage());
		}
		courseAnnouncement.setDate(LocalDateTime.now());
		return repository.save(courseAnnouncement);
	}

	public boolean delete(Long id, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		CourseAnnouncement courseAnnouncement = repository.findById(id).orElse(null);
		if (courseAnnouncement == null || !canManage(courseAnnouncement.getCourse(), requestingUserId, isAdmin, isStudentAffairs)) {
			return false;
		}
		repository.deleteById(id);
		return true;
	}

	private boolean canManage(Course course, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		if (isAdmin) {
			return true;
		}
		if (isStudentAffairs) {
			StudentAffairsOffice office = studentAffairsOfficeRepository.findById(requestingUserId).orElse(null);
			Faculty courseFaculty = course.getStudyProgram() != null ? course.getStudyProgram().getFaculty() : null;
			return office != null && office.getFaculty() != null && courseFaculty != null
					&& office.getFaculty().getId().equals(courseFaculty.getId());
		}
		return course.getTeacher().getId().equals(requestingUserId);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(CourseAnnouncement courseAnnouncement) {
		repository.delete(courseAnnouncement);
	}
}
