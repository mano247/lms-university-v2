package com.lmsuniversity.announcement;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseAnnouncementRepository extends JpaRepository<CourseAnnouncement, Long>{

	String DETAILS_FETCH =
			"JOIN FETCH ca.course c "
			+ "JOIN FETCH c.teacher "
			+ "JOIN FETCH c.studyProgram sp "
			+ "JOIN FETCH sp.faculty f "
			+ "JOIN FETCH f.university "
			+ "LEFT JOIN FETCH c.teachingMaterials ";

	@Query(value = "SELECT DISTINCT ca FROM CourseAnnouncement ca "
			+ "JOIN ca.course c "
			+ "LEFT JOIN c.students s "
			+ "WHERE s.id = :userId OR c.teacher.id = :userId",
		countQuery = "SELECT count(DISTINCT ca) FROM CourseAnnouncement ca "
			+ "JOIN ca.course c "
			+ "LEFT JOIN c.students s "
			+ "WHERE s.id = :userId OR c.teacher.id = :userId")
	Page<CourseAnnouncement> findVisibleToUser(@Param("userId") Long userId, Pageable pageable);

	@Query("SELECT DISTINCT ca FROM CourseAnnouncement ca "
			+ "JOIN ca.course c "
			+ "LEFT JOIN c.students s "
			+ "WHERE c.id = :courseId AND (s.id = :userId OR c.teacher.id = :userId)")
	List<CourseAnnouncement> findByCourseVisibleToUser(@Param("courseId") Long courseId, @Param("userId") Long userId);

	@Query("SELECT DISTINCT ca FROM CourseAnnouncement ca " + DETAILS_FETCH + "WHERE ca IN :courseAnnouncements")
	List<CourseAnnouncement> fetchDetails(@Param("courseAnnouncements") List<CourseAnnouncement> courseAnnouncements);

	@Override
	@EntityGraph(attributePaths = {
			"course.teacher", "course.studyProgram.faculty.university",
			"course.teachingMaterials", "course.students"})
	Optional<CourseAnnouncement> findById(Long id);

}
