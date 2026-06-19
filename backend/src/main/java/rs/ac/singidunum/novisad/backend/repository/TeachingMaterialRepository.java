package rs.ac.singidunum.novisad.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.singidunum.novisad.backend.model.academic.TeachingMaterial;

@Repository
public interface TeachingMaterialRepository extends JpaRepository<TeachingMaterial, Long>{

}
