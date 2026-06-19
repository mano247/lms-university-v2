package rs.ac.singidunum.novisad.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.singidunum.novisad.backend.model.StudentYearEnrollment;

@Repository
public interface StudentYearEnrollmentRepository extends JpaRepository<StudentYearEnrollment, Long>{

}
