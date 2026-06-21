package com.lmsuniversity.faculty;

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
public class FacultyCreateDto {
	@NotBlank
	private String facultyCode;

	@NotBlank
	private String name;
	private String contact;
	private String description;
	private Long deanId;
	private String address;

	@NotNull
	private Long universityId;
}
