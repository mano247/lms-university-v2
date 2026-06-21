package com.lmsuniversity.studentyearenrollment;

import java.sql.Date;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentYearEnrollmentCreateDto {
	@NotNull
	private Date enrollmentDate;

	@NotNull
	private Long studentId;

	@NotNull
	private Long studyYearId;

	@NotNull
	private Long studyProgramId;
}
