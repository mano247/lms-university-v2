package com.lmsuniversity.examperiod;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamPeriodTermDto {
	private Long id;
	private LocalDateTime dateTime;
	private String classroom;
	private int maxStudents;

	@Builder.Default
	private long registeredCount = 0;
}
