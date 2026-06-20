package com.lmsuniversity.announcement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseAnnouncementRepository extends JpaRepository<CourseAnnouncement, Long>{

}
