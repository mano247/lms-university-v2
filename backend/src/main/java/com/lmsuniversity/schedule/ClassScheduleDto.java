package com.lmsuniversity.schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.lmsuniversity.course.CourseDto;
import com.lmsuniversity.user.TeacherDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassScheduleDto {
	private Long id;
	private CourseDto course;
	private TeacherDto teacher;
	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;
	private String classroom;
	private ClassType type;
}
