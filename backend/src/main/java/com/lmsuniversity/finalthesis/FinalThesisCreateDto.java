package com.lmsuniversity.finalthesis;

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
public class FinalThesisCreateDto {
	@NotBlank
	private String topic;
	private String link;

	@NotNull
	private Long studentId;

	@NotNull
	private Long mentorId;
}
