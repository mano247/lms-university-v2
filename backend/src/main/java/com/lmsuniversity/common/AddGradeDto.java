package com.lmsuniversity.common;

import jakarta.persistence.Column;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddGradeDto {

	private Long courseId;
	private String courseName;
	private int ects;
	@Column(columnDefinition = "LONGTEXT")
	private String syllabus;

	private List<TeacherStudentsDto> studentsInCourse;
}
