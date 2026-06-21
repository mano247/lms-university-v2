package com.lmsuniversity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentListDto {
	private Long id;
	private String email;
	private String username;
	private String indexNumber;
	private String firstName;
	private String lastName;
	private Long facultyId;
	private String facultyName;
}
