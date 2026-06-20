package com.lmsuniversity.announcement;

import jakarta.persistence.*;
import com.lmsuniversity.course.Course;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class CourseAnnouncement extends Announcement {

	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;
}
