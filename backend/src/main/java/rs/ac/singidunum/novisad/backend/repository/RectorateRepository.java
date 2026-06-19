package rs.ac.singidunum.novisad.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.singidunum.novisad.backend.model.Rectorate;


@Repository
public interface RectorateRepository extends JpaRepository<Rectorate, Long>{


}
