package com.lmsuniversity.user;

import java.util.HashSet;
import java.util.Set;

import com.lmsuniversity.faculty.FacultyDto;
import com.lmsuniversity.permission.Permission;
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
	private String email;
	private String username;
	private String indexNumber;
	private String firstName;
	private String lastName;
	private FacultyDto faculty;

	@Builder.Default
	private Set<Permission> permission = new HashSet<>();
}
