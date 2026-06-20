package com.lmsuniversity.announcement;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import com.lmsuniversity.course.Course;

@Entity
public class CourseAnnouncement extends Announcement{

    @ManyToOne
    @JoinColumn(name = "predmet_id")
    private Course course;

    public CourseAnnouncement() {
		super();
	}


	public CourseAnnouncement(Long id, LocalDateTime date, String content, String title, String image,
			Date startDate, Date endDate, Course course) {
		super(id,  content, title, date, image, startDate, endDate);
		this.course = course;
	}


	public CourseAnnouncement(Course course) {
		super();
		this.course = course;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
