package rs.ac.singidunum.novisad.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.singidunum.novisad.backend.model.academic.StudyProgram;


@Repository
public interface StudyProgramRepository extends JpaRepository<StudyProgram, Long>{

	Optional<StudyProgram> findByProgramCode(String programCode);

}
