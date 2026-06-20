package com.lmsuniversity.studyyear;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyYearRepository extends JpaRepository<StudyYear, Long>{

}
