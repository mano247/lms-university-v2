package com.lmsuniversity.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCoursesDto {

	private String courseName;
	private int ects;
	private Double points;
	private int grade;
}
