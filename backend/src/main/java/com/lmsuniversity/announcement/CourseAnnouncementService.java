package com.lmsuniversity.announcement;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

	public Page<CourseAnnouncement> findVisibleToUser(Long userId, Pageable pageable) {
		Page<CourseAnnouncement> page = repository.findVisibleToUser(userId, pageable);
		List<CourseAnnouncement> withDetails = repository.fetchDetails(page.getContent());
		Map<Long, CourseAnnouncement> byId = new LinkedHashMap<>();
		withDetails.forEach(ca -> byId.put(ca.getId(), ca));
		List<CourseAnnouncement> ordered = page.getContent().stream().map(ca -> byId.get(ca.getId())).toList();
		return new PageImpl<>(ordered, pageable, page.getTotalElements());
	}

	public List<CourseAnnouncement> findByCourseVisibleToUser(Long courseId, Long userId) {
		List<CourseAnnouncement> matches = repository.findByCourseVisibleToUser(courseId, userId);
		return repository.fetchDetails(matches);
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
