package com.lmsuniversity.examattempt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long>{

	@SuppressWarnings("unused")
	private Iterable<ExamAttempt> findAllByStudent(Long id){
        List<ExamAttempt> examAttempts = new ArrayList<>();
        for(ExamAttempt pp : findAll()){
            if(pp.getStudent().getId().equals(id)) {
                examAttempts.add(pp);
            }
        }
        return examAttempts;
    };
}
