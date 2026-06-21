package com.lmsuniversity.finalthesis;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FinalThesisRepository extends JpaRepository<FinalThesis, Long>{

	boolean existsByStudentId(Long studentId);

	boolean existsByMentorId(Long mentorId);

	@Override
	@EntityGraph(attributePaths = {"student", "mentor"})
	Page<FinalThesis> findAll(Pageable pageable);

	@EntityGraph(attributePaths = {"student", "mentor"})
	Optional<FinalThesis> findByStudentId(Long studentId);

	@EntityGraph(attributePaths = {"student", "mentor"})
	List<FinalThesis> findByMentorId(Long mentorId);

}
