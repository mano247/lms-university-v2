package com.lmsuniversity.examperiod;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamPeriodTermRepository extends JpaRepository<ExamPeriodTerm, Long> {

	@Override
	@EntityGraph(attributePaths = {"examPeriod.course.teacher",
			"examPeriod.course.studyProgram.faculty.university"})
	Optional<ExamPeriodTerm> findById(Long id);

}
