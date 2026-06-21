package com.lmsuniversity.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAffairsOfficeRepository extends JpaRepository<StudentAffairsOffice, Long>{

	@Override
	@EntityGraph(attributePaths = {"faculty"})
	Page<StudentAffairsOffice> findAll(Pageable pageable);

}
