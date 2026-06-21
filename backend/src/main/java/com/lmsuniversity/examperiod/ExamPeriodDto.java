package com.lmsuniversity.examperiod;

import java.time.LocalDate;
import java.util.List;

import com.lmsuniversity.course.CourseDto;
import com.lmsuniversity.user.RegisteredUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamPeriodDto {
	private Long id;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	private CourseDto course;
	private RegisteredUserDto createdBy;
	private List<ExamPeriodTermDto> terms;
}
