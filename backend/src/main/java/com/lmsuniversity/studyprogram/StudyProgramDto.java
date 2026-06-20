package com.lmsuniversity.studyprogram;

import java.util.Set;

import com.lmsuniversity.faculty.FacultyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyProgramDto {

	private Long id;
	private String programCode;
	private String description;
	private String name;
	private String programDirector;
	private FacultyDto faculty;
	private Set<String> courses;
}
