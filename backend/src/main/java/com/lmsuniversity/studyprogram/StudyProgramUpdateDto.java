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
public class StudyProgramUpdateDto {
	@NotBlank
	private String programCode;
	private String description;

	@NotBlank
	private String name;
	private String programDirector;

	@NotNull
	private Long facultyId;
}
