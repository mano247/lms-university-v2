package com.lmsuniversity.schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.EnumType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.lmsuniversity.course.Course;
import com.lmsuniversity.user.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClassSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	@NotNull
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;

	@NotNull
	private LocalTime startTime;

	@NotNull
	private LocalTime endTime;

	@NotBlank
	private String classroom;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ClassType type;

	@AssertTrue(message = "endTime must not be before startTime")
	public boolean isTimeRangeValid() {
		if (startTime == null || endTime == null) {
			return true;
		}
		return endTime.isAfter(startTime);
	}
}
