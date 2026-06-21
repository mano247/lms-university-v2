package com.lmsuniversity.announcement;

import java.util.Date;

import jakarta.validation.constraints.AssertTrue;
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
public class CourseAnnouncementCreateDto {
	@NotBlank
	private String title;

	@NotBlank
	private String content;

	private String image;
	private Date startDate;
	private Date endDate;

	@NotNull
	private Long courseId;

	@AssertTrue(message = "endDate must not be before startDate")
	public boolean isDateRangeValid() {
		if (startDate == null || endDate == null) {
			return true;
		}
		return !endDate.before(startDate);
	}
}
