package rs.ac.singidunum.novisad.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.singidunum.novisad.backend.model.academic.Course;


@Repository
public interface PredmetRepository extends JpaRepository<Course, Long>{

	Optional<Course> findBysifraPredmeta(String sifraPredmet);

}

