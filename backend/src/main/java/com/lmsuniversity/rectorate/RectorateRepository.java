package com.lmsuniversity.rectorate;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RectorateRepository extends JpaRepository<Rectorate, Long>{

	@Override
	@EntityGraph(attributePaths = {"universities"})
	List<Rectorate> findAll();

}
