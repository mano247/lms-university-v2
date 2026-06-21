package com.lmsuniversity.announcement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository  extends JpaRepository<Announcement, Long>{

	@Query(value = "SELECT a FROM Announcement a WHERE TYPE(a) = Announcement",
			countQuery = "SELECT count(a) FROM Announcement a WHERE TYPE(a) = Announcement")
	Page<Announcement> findAllGlobal(Pageable pageable);

}
