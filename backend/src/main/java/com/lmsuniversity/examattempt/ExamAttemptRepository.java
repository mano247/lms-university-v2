package com.lmsuniversity.examattempt;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long>{

	@EntityGraph(attributePaths = {"course", "student", "teacher"})
	List<ExamAttempt> findByStudentId(Long studentId);

	boolean existsByCourseId(Long courseId);

	boolean existsByStudentId(Long studentId);

	boolean existsByTeacherId(Long teacherId);

	boolean existsByExamPeriodTermId(Long examPeriodTermId);

	boolean existsByExamPeriodTermIdAndStudentId(Long examPeriodTermId, Long studentId);

	long countByExamPeriodTermId(Long examPeriodTermId);

	@Query("SELECT (COUNT(e) > 0) FROM ExamAttempt e WHERE e.examPeriodTerm.examPeriod.id = :examPeriodId")
	boolean existsByExamPeriodId(@Param("examPeriodId") Long examPeriodId);

	String DETAILS_FETCH =
			"JOIN FETCH e.course c "
			+ "JOIN FETCH c.teacher "
			+ "JOIN FETCH c.studyProgram sp "
			+ "JOIN FETCH sp.faculty f "
			+ "JOIN FETCH f.university "
			+ "LEFT JOIN FETCH c.teachingMaterials "
			+ "JOIN FETCH e.student s "
			+ "LEFT JOIN FETCH s.faculty "
			+ "JOIN FETCH e.teacher ";

	@Query("SELECT DISTINCT e FROM ExamAttempt e " + DETAILS_FETCH + "WHERE e IN :examAttempts")
	List<ExamAttempt> fetchDetails(@Param("examAttempts") List<ExamAttempt> examAttempts);

	@Query("SELECT DISTINCT e FROM ExamAttempt e " + DETAILS_FETCH
			+ "WHERE e.finalGrade = 0 AND e.points = 0.0 AND e.student.id = :studentId")
	List<ExamAttempt> findRegisteredByStudent(@Param("studentId") Long studentId);

	@Query("SELECT DISTINCT e FROM ExamAttempt e " + DETAILS_FETCH
			+ "WHERE e.finalGrade = 0 AND e.points = 0.0 AND e.course.id = :courseId")
	List<ExamAttempt> findRegisteredByCourse(@Param("courseId") Long courseId);

	@Query("SELECT DISTINCT e FROM ExamAttempt e JOIN FETCH e.student JOIN FETCH e.course WHERE e.course.id IN :courseIds")
	List<ExamAttempt> findByCourseIds(@Param("courseIds") List<Long> courseIds);

}
