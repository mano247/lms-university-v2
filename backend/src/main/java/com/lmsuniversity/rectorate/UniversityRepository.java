package com.lmsuniversity.rectorate;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long>{

	@Override
	@EntityGraph(attributePaths = {"rectorate"})
	List<University> findAll();

}
