package com.lmsuniversity.announcement;

import java.time.LocalDateTime;
import java.util.Date;

import com.lmsuniversity.course.CourseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseAnnouncementDto {
	private Long id;
	private String title;
	private String content;
	private LocalDateTime date;
	private String imageUrl;
	private CourseDto course;
	private Date startDate;
	private Date endDate;
}
