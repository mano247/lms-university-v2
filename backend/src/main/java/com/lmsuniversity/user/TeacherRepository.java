package com.lmsuniversity.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeacherRepository extends JpaRepository <Teacher, Long>{

	boolean existsByUniversityId(Long universityId);

	@EntityGraph(attributePaths = {"university", "courses"})
	Optional<Teacher> findWithCoursesById(Long id);

}
