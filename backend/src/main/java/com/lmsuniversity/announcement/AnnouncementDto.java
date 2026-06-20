package com.lmsuniversity.announcement;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementDto {

	private Long id;
	private LocalDateTime postedAt;
	private String content;
	private String title;
	private String image;
	private Date startDate;
	private Date endDate;
}
