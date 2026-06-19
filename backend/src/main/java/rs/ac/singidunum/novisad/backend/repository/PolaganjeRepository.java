package rs.ac.singidunum.novisad.backend.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.singidunum.novisad.backend.model.ExamAttempt;

@Repository
public interface PolaganjeRepository extends JpaRepository<ExamAttempt, Long>{

	@SuppressWarnings("unused")
	private Iterable<ExamAttempt> findAllByStudent(Long id){
        List<ExamAttempt> examAttempts = new ArrayList<>();
        for(ExamAttempt pp : findAll()){
            if(pp.getStudent().getId() == id) {
                examAttempts.add(pp);
            }
        }
        return examAttempts;
    };
}
