package com.lmsuniversity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface StudentAffairsOfficeRepository extends JpaRepository<StudentAffairsOffice, Long>{

}
