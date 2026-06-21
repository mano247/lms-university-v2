package com.lmsuniversity.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

	@Query("SELECT new com.lmsuniversity.user.StudentListDto("
			+ "s.id, s.email, s.username, s.indexNumber, s.firstName, s.lastName, "
			+ "f.id, f.name) "
			+ "FROM Student s LEFT JOIN s.faculty f")
	Page<StudentListDto> findAllProjected(Pageable pageable);

	boolean existsByFacultyId(Long facultyId);

}
