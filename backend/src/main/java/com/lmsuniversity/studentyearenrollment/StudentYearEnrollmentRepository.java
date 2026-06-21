package com.lmsuniversity.studentyearenrollment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentYearEnrollmentRepository extends JpaRepository<StudentYearEnrollment, Long>{

	@Override
	@EntityGraph(attributePaths = {"student.faculty", "studyYear", "studyProgram.faculty.university"})
	Page<StudentYearEnrollment> findAll(Pageable pageable);

	@EntityGraph(attributePaths = {"student.faculty", "studyYear", "studyProgram.faculty.university"})
	List<StudentYearEnrollment> findByStudentId(Long studentId);

	boolean existsByStudentId(Long studentId);

	boolean existsByStudyProgramId(Long studyProgramId);

}
