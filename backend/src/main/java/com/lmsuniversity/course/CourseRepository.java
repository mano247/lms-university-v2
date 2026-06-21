package com.lmsuniversity.course;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{

	Optional<Course> findByCourseCode(String courseCode);

	boolean existsByStudyProgramId(Long studyProgramId);

	boolean existsByTeacherId(Long teacherId);

	List<Course> findByStudyProgramIdAndStudyYearId(Long studyProgramId, Long studyYearId);

	@Query("SELECT DISTINCT c FROM Course c "
			+ "JOIN FETCH c.teacher "
			+ "JOIN FETCH c.studyProgram sp "
			+ "JOIN FETCH sp.faculty f "
			+ "JOIN FETCH f.university "
			+ "LEFT JOIN FETCH c.teachingMaterials "
			+ "WHERE c IN :courses")
	List<Course> fetchDetails(@Param("courses") List<Course> courses);

}
