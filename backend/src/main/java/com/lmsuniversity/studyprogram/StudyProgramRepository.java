package com.lmsuniversity.studyprogram;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyProgramRepository extends JpaRepository<StudyProgram, Long>{

	Optional<StudyProgram> findByProgramCode(String programCode);

	boolean existsByFacultyId(Long facultyId);

	@Override
	@EntityGraph(attributePaths = {"faculty.university", "courses"})
	List<StudyProgram> findAll();

}
