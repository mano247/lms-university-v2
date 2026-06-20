package com.lmsuniversity.course;

import java.util.Date;
import java.util.Set;

import com.lmsuniversity.studyprogram.StudyProgramDto;
import com.lmsuniversity.teachingmaterial.TeachingMaterialDto;
import com.lmsuniversity.user.TeacherDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
	private Long id;
	private String courseCode;
	private String syllabus;
	private String name;
	private int ects;
	private TeacherDto teacher;
	private Date startDate;
	private Date endDate;
	private String description;
	private StudyProgramDto studyProgram;
	private Set<TeachingMaterialDto> teachingMaterials;
}
