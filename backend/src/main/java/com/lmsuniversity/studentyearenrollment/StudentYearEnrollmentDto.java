package com.lmsuniversity.studentyearenrollment;

import java.sql.Date;

import com.lmsuniversity.studyyear.StudyYearDto;
import com.lmsuniversity.studyprogram.StudyProgramDto;
import com.lmsuniversity.user.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentYearEnrollmentDto {

	private Long id;
	private Date enrollmentDate;
	private StudyYearDto studyYear;
	private StudentDto student;
	private StudyProgramDto studyProgram;
}
