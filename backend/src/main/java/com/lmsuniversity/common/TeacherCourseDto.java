package com.lmsuniversity.common;

import java.util.Date;
import java.util.Set;

import com.lmsuniversity.teachingmaterial.TeachingMaterialDto;
import com.lmsuniversity.user.TeacherDto;
import com.lmsuniversity.user.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherCourseDto {
	private Long id;
	private String courseCode;
	private String syllabus;
	private String name;
	private int ects;
	private TeacherDto teacher;
	private Date startDate;
	private Date endDate;
	private String description;
	private String studyProgram;
	private Set<TeachingMaterialDto> teachingMaterials;
	private Set<StudentDto> students;
}
