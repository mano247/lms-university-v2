package com.lmsuniversity.faculty;

import com.lmsuniversity.rectorate.UniversityDto;
import com.lmsuniversity.user.TeacherDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacultyDto {

	private Long id;
	private String facultyCode;
	private String name;
	private String contact;
	private String description;
	private TeacherDto dean;
	private String image;
	private String address;
	private UniversityDto university;
}
