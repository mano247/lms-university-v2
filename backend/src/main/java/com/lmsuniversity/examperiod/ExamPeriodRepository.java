package com.lmsuniversity.examperiod;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamPeriodRepository extends JpaRepository<ExamPeriod, Long> {

	@Override
	@EntityGraph(attributePaths = {"course.teacher", "course.studyProgram.faculty.university",
			"course.teachingMaterials", "createdBy", "terms"})
	Page<ExamPeriod> findAll(Pageable pageable);

	@EntityGraph(attributePaths = {"course.teacher", "course.studyProgram.faculty.university",
			"course.teachingMaterials", "createdBy", "terms"})
	Page<ExamPeriod> findByCourse_StudyProgram_Faculty_Id(Long facultyId, Pageable pageable);

	@EntityGraph(attributePaths = {"course.teacher", "course.studyProgram.faculty.university",
			"course.teachingMaterials", "createdBy", "terms"})
	Page<ExamPeriod> findByCourseId(Long courseId, Pageable pageable);

	@Override
	@EntityGraph(attributePaths = {"course.teacher", "course.studyProgram.faculty.university",
			"course.teachingMaterials", "createdBy", "terms"})
	Optional<ExamPeriod> findById(Long id);

	@EntityGraph(attributePaths = {"course.teacher", "course.studyProgram.faculty.university",
			"course.teachingMaterials", "createdBy", "terms"})
	@Query("SELECT p FROM ExamPeriod p WHERE p.course.id = :courseId AND :today BETWEEN p.startDate AND p.endDate")
	Page<ExamPeriod> findOpenByCourseId(@Param("courseId") Long courseId, @Param("today") LocalDate today, Pageable pageable);

	boolean existsByCourseId(Long courseId);

	@EntityGraph(attributePaths = {"course.teacher", "course.studyProgram.faculty.university",
			"course.teachingMaterials", "createdBy", "terms"})
	Page<ExamPeriod> findByCourse_Teacher_Id(Long teacherId, Pageable pageable);

	@EntityGraph(attributePaths = {"course.teacher", "course.studyProgram.faculty.university",
			"course.teachingMaterials", "createdBy", "terms"})
	@Query("SELECT ep FROM ExamPeriod ep JOIN ep.course.students s WHERE s.id = :studentId AND :today BETWEEN ep.startDate AND ep.endDate")
	Page<ExamPeriod> findOpenByStudentId(@Param("studentId") Long studentId, @Param("today") LocalDate today, Pageable pageable);

}
