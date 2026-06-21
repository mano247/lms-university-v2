package com.lmsuniversity.user;

import com.lmsuniversity.faculty.FacultyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAffairsOfficeDto {

	private String userType;
	private Long id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private FacultyDto faculty;
}
