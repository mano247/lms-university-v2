package com.lmsuniversity.faculty;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long>{

//	Optional<Faculty> findOne(Long Id);

	Optional<Faculty> findByFacultyCode(String facultyCode);

}

