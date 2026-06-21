package com.lmsuniversity.schedule;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {

	@Override
	@EntityGraph(attributePaths = {"course.studyProgram.faculty.university", "course.teachingMaterials", "teacher"})
	Page<ClassSchedule> findAll(Pageable pageable);

	@EntityGraph(attributePaths = {"course.studyProgram.faculty.university", "course.teachingMaterials", "teacher"})
	Page<ClassSchedule> findByCourse_StudyProgram_Faculty_Id(Long facultyId, Pageable pageable);

	@EntityGraph(attributePaths = {"course.studyProgram.faculty.university", "course.teachingMaterials", "teacher"})
	Page<ClassSchedule> findByTeacherId(Long teacherId, Pageable pageable);

	@EntityGraph(attributePaths = {"course.studyProgram.faculty.university", "course.teachingMaterials", "teacher"})
	Page<ClassSchedule> findByCourse_StudentsId(Long studentId, Pageable pageable);

	List<ClassSchedule> findByClassroomAndDayOfWeek(String classroom, DayOfWeek dayOfWeek);

}
