package com.lmsuniversity.examattempt;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamAttemptCreateDto {
	@NotNull
	private Long courseId;

	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String note;
}
