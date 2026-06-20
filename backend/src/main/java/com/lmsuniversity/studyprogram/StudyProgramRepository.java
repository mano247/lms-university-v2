package com.lmsuniversity.studyprogram;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyProgramRepository extends JpaRepository<StudyProgram, Long>{

	Optional<StudyProgram> findByProgramCode(String programCode);

}
