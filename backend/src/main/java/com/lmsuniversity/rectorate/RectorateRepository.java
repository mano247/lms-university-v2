package com.lmsuniversity.rectorate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RectorateRepository extends JpaRepository<Rectorate, Long>{


}
