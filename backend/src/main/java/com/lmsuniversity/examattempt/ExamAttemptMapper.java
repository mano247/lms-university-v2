package com.lmsuniversity.examattempt;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lmsuniversity.course.CourseMapper;
import com.lmsuniversity.user.StudentMapper;
import com.lmsuniversity.user.TeacherMapper;

@Mapper(componentModel = "spring", uses = { CourseMapper.class, StudentMapper.class, TeacherMapper.class })
public interface ExamAttemptMapper {

	@Mapping(target = "student", source = "student", qualifiedByName = "toSummaryDto")
	ExamAttemptDto toDto(ExamAttempt examAttempt);

	List<ExamAttemptDto> toDtoList(List<ExamAttempt> examAttempts);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course", ignore = true)
	@Mapping(target = "student", ignore = true)
	@Mapping(target = "teacher", ignore = true)
	@Mapping(target = "announcement", ignore = true)
	@Mapping(target = "points", ignore = true)
	@Mapping(target = "finalGrade", ignore = true)
	ExamAttempt toEntity(ExamAttemptCreateDto dto);
}
