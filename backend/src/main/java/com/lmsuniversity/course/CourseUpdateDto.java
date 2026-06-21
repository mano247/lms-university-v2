package com.lmsuniversity.course;

import java.util.Date;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseUpdateDto {
	@NotBlank
	private String courseCode;
	private String syllabus;

	@NotBlank
	private String name;

	@Min(1)
	private int ects;

	private Date startDate;
	private Date endDate;
	private String description;

	@NotNull
	private Long teacherId;

	@NotNull
	private Long studyProgramId;

	@AssertTrue(message = "endDate must not be before startDate")
	public boolean isDateRangeValid() {
		if (startDate == null || endDate == null) {
			return true;
		}
		return !endDate.before(startDate);
	}
}
