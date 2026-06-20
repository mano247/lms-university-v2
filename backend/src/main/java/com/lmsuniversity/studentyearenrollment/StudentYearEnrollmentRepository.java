package com.lmsuniversity.studentyearenrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentYearEnrollmentRepository extends JpaRepository<StudentYearEnrollment, Long>{

}
