package com.lmsuniversity.finalthesis;

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
public class FinalThesisDto {
	private Long id;
	private String topic;
	private String link;
	private StudentDto student;
	private TeacherDto mentor;
}
