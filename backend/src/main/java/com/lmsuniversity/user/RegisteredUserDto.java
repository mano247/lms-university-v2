package com.lmsuniversity.user;

import java.util.HashSet;
import java.util.Set;

import com.lmsuniversity.permission.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisteredUserDto {

	private String userType;
	private Long id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;

	@Builder.Default
	private Set<Permission> permission = new HashSet<>();
}
