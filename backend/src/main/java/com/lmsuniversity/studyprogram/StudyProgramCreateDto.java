package com.lmsuniversity.studyprogram;

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
public class StudyProgramCreateDto {
	@NotBlank
	private String programCode;
	private String description;

	@NotBlank
	private String name;
	private Long programDirectorId;

	@NotNull
	private Long facultyId;
}
