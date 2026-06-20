package com.lmsuniversity.user;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lmsuniversity.permission.Permission;
import com.lmsuniversity.faculty.Faculty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {

	private String userType;
	private Long id;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String username;

	@NotBlank
	private String indexNumber;

	// Only used as input on the enrollStudent path; never serialized back out.
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank
	@Size(min = 8)
	private String password;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;
	private Faculty faculty;

	@Builder.Default
	private Set<Permission> permission = new HashSet<>();
}
