package com.lmsuniversity.examperiod;

import java.time.LocalDateTime;

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
public class ExamPeriodTermUpdateDto {
	@NotNull
	private LocalDateTime dateTime;

	@NotBlank
	private String classroom;

	@Min(1)
	private int maxStudents;
}
