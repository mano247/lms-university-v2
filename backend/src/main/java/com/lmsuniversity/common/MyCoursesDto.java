package com.lmsuniversity.common;

import com.lmsuniversity.user.TeacherDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyCoursesDto {
	private Long id;
	private String courseName;
	private String description;
	private String syllabus;
	private TeacherDto teacher;
	private Double points;
	private int ects;
	private int grade;
}
