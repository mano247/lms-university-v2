package com.lmsuniversity.examattempt;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamAttemptUpdateDto {
	@DecimalMin("0.0")
	private Double points;

	@Min(0)
	private Integer finalGrade;

	private String note;
}
