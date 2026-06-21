package com.lmsuniversity.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollStudentDto {
	@NotBlank
	@Size(max = 50)
	private String username;

	@NotBlank
	@Email
	@Size(max = 100)
	private String email;

	@NotBlank
	@Size(min = 8)
	private String password;

	@NotBlank
	@Size(max = 50)
	private String firstName;

	@NotBlank
	@Size(max = 50)
	private String lastName;

	@NotBlank
	@Size(max = 20)
	private String indexNumber;

	@NotNull
	private Long facultyId;

	@NotNull
	private Long studyProgramId;

	@NotNull
	private Long studyYearId;
}
