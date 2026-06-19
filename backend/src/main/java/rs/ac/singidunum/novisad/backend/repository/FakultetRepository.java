package rs.ac.singidunum.novisad.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.singidunum.novisad.backend.model.academic.Faculty;



@Repository
public interface FakultetRepository extends JpaRepository<Faculty, Long>{
	
//	Optional<Faculty> findOne(Long Id);

	Optional<Faculty> findBySifraFakulteta(String facultyCode);

}

