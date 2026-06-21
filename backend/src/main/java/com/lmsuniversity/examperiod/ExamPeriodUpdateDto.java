package com.lmsuniversity.examperiod;

import java.time.LocalDate;

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
public class ExamPeriodUpdateDto {
	@NotBlank
	private String name;

	@NotNull
	private LocalDate startDate;

	@NotNull
	private LocalDate endDate;

	@AssertTrue(message = "endDate must not be before startDate")
	public boolean isDateRangeValid() {
		if (startDate == null || endDate == null) {
			return true;
		}
		return !endDate.isBefore(startDate);
	}
}
