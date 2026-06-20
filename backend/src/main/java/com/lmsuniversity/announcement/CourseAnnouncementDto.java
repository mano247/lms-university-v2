package com.lmsuniversity.announcement;

import java.time.LocalDateTime;
import java.util.Date;

import com.lmsuniversity.course.CourseDto;

public class CourseAnnouncementDto {
    private Long id;

    private String title;

    private String content;

    private LocalDateTime date;

    private String imageUrl;

    private CourseDto course;

    private Date startDate;

	private Date endDate;

	public CourseAnnouncementDto() {
		super();
	}

	public CourseAnnouncementDto(Long id, String title, String content, LocalDateTime date, String imageUrl,
			CourseDto course, Date startDate, Date endDate) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.date = date;
		this.imageUrl = imageUrl;
		this.course = course;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public CourseAnnouncementDto(CourseDto course) {
		super();
		this.course = course;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public CourseDto getCourse() {
		return course;
	}

	public void setCourse(CourseDto course) {
		this.course = course;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
