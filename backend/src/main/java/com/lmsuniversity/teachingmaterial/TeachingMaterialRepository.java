package com.lmsuniversity.teachingmaterial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingMaterialRepository extends JpaRepository<TeachingMaterial, Long>{

}
