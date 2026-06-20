package com.lmsuniversity.finalthesis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FinalThesisRepository extends JpaRepository<FinalThesis, Long>{

}
