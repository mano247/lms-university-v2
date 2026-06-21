package com.lmsuniversity.faculty;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long>{

	@Override
	@EntityGraph(attributePaths = {"university", "dean"})
	List<Faculty> findAll();

	Optional<Faculty> findByFacultyCode(String facultyCode);

	boolean existsByUniversityId(Long universityId);

	boolean existsByDeanId(Long deanId);

}
