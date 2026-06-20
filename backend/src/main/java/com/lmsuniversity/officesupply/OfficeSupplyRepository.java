package com.lmsuniversity.officesupply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeSupplyRepository extends JpaRepository<OfficeSupply, Long>{

}
