package com.lmsuniversity.schedule;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lmsuniversity.course.Course;
import com.lmsuniversity.course.CourseRepository;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.user.StudentAffairsOffice;
import com.lmsuniversity.user.StudentAffairsOfficeRepository;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.TeacherRepository;

@Service
public class ClassScheduleService {

	@Autowired
	private ClassScheduleRepository repository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private StudentAffairsOfficeRepository studentAffairsOfficeRepository;

	@Autowired
	private ClassScheduleMapper mapper;

	public Optional<ClassSchedule> findOne(Long id) {
		return repository.findById(id);
	}

	public boolean isVisibleToStaff(ClassSchedule schedule, Long userId, boolean isAdmin, boolean isStudentAffairs) {
		return canManageCourse(schedule.getCourse(), userId, isAdmin, isStudentAffairs);
	}

	public Page<ClassSchedule> findVisibleToStaff(Long userId, boolean isAdmin, boolean isStudentAffairs, Pageable pageable) {
		if (isAdmin) {
			return repository.findAll(pageable);
		}
		StudentAffairsOffice office = studentAffairsOfficeRepository.findById(userId).orElse(null);
		if (office == null || office.getFaculty() == null) {
			return Page.empty(pageable);
		}
		return repository.findByCourse_StudyProgram_Faculty_Id(office.getFaculty().getId(), pageable);
	}

	public Page<ClassSchedule> findMyTeaching(Long teacherId, Pageable pageable) {
		return repository.findByTeacherId(teacherId, pageable);
	}

	public Page<ClassSchedule> findMySchedule(Long studentId, Pageable pageable) {
		return repository.findByCourse_StudentsId(studentId, pageable);
	}

	public ClassSchedule create(ClassScheduleCreateDto dto, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		Course course = courseRepository.findById(dto.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException("Course not found: " + dto.getCourseId()));
		if (!canManageCourse(course, requestingUserId, isAdmin, isStudentAffairs)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"You are not allowed to schedule classes for this course.");
		}
		Teacher teacher = teacherRepository.findById(dto.getTeacherId())
				.orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + dto.getTeacherId()));

		ClassSchedule schedule = mapper.toEntity(dto);
		schedule.setCourse(course);
		schedule.setTeacher(teacher);
		assertNoOverlap(schedule, null);
		return repository.save(schedule);
	}

	public ClassSchedule update(Long id, ClassScheduleUpdateDto dto, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		ClassSchedule schedule = repository.findById(id).orElse(null);
		if (schedule == null || !canManageCourse(schedule.getCourse(), requestingUserId, isAdmin, isStudentAffairs)) {
			return null;
		}
		Course course = courseRepository.findById(dto.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException("Course not found: " + dto.getCourseId()));
		if (!canManageCourse(course, requestingUserId, isAdmin, isStudentAffairs)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"You are not allowed to schedule classes for this course.");
		}
		Teacher teacher = teacherRepository.findById(dto.getTeacherId())
				.orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + dto.getTeacherId()));

		mapper.updateEntityFromDto(dto, schedule);
		schedule.setCourse(course);
		schedule.setTeacher(teacher);
		assertNoOverlap(schedule, id);
		return repository.save(schedule);
	}

	public boolean delete(Long id, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		ClassSchedule schedule = repository.findById(id).orElse(null);
		if (schedule == null || !canManageCourse(schedule.getCourse(), requestingUserId, isAdmin, isStudentAffairs)) {
			return false;
		}
		repository.deleteById(id);
		return true;
	}

	private void assertNoOverlap(ClassSchedule candidate, Long excludeId) {
		List<ClassSchedule> existing = repository.findByClassroomAndDayOfWeek(candidate.getClassroom(), candidate.getDayOfWeek());
		for (ClassSchedule other : existing) {
			if (excludeId != null && other.getId().equals(excludeId)) {
				continue;
			}
			boolean overlaps = candidate.getStartTime().isBefore(other.getEndTime())
					&& other.getStartTime().isBefore(candidate.getEndTime());
			if (overlaps) {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"Classroom " + candidate.getClassroom() + " is already booked on " + candidate.getDayOfWeek()
								+ " from " + other.getStartTime() + " to " + other.getEndTime() + ".");
			}
		}
	}

	private boolean canManageCourse(Course course, Long requestingUserId, boolean isAdmin, boolean isStudentAffairs) {
		if (isAdmin) {
			return true;
		}
		if (isStudentAffairs) {
			StudentAffairsOffice office = studentAffairsOfficeRepository.findById(requestingUserId).orElse(null);
			Faculty courseFaculty = course.getStudyProgram() != null ? course.getStudyProgram().getFaculty() : null;
			return office != null && office.getFaculty() != null && courseFaculty != null
					&& office.getFaculty().getId().equals(courseFaculty.getId());
		}
		return false;
	}
}
