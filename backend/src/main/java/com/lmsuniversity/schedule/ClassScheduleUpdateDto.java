package com.lmsuniversity.schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

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
public class ClassScheduleUpdateDto {
	@NotNull
	private Long courseId;

	@NotNull
	private Long teacherId;

	@NotNull
	private DayOfWeek dayOfWeek;

	@NotNull
	private LocalTime startTime;

	@NotNull
	private LocalTime endTime;

	@NotBlank
	private String classroom;

	@NotNull
	private ClassType type;

	@AssertTrue(message = "endTime must not be before startTime")
	public boolean isTimeRangeValid() {
		if (startTime == null || endTime == null) {
			return true;
		}
		return endTime.isAfter(startTime);
	}
}
