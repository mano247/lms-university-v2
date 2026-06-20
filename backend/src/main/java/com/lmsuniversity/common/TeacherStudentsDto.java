package com.lmsuniversity.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherStudentsDto {

	private Long studentId;
	private Long examAttemptId;
	private String studentFirstName;
	private String studentLastName;
	private String indexNumber;
	private Double points;
	private int grade;
}
