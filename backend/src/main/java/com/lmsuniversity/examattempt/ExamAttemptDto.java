package com.lmsuniversity.examattempt;

import java.time.LocalDateTime;

import com.lmsuniversity.course.CourseDto;
import com.lmsuniversity.user.StudentDto;
import com.lmsuniversity.user.TeacherDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamAttemptDto {
	private Long id;
	private double points;
	private int finalGrade;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String note;
	private StudentDto student;
	private CourseDto course;
	private TeacherDto teacher;
}
