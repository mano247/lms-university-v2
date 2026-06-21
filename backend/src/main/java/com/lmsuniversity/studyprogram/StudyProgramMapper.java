package com.lmsuniversity.studyprogram;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.lmsuniversity.course.Course;
import com.lmsuniversity.faculty.FacultyMapper;

@Mapper(componentModel = "spring", uses = { FacultyMapper.class })
public interface StudyProgramMapper {

	StudyProgramDto toDto(StudyProgram studyProgram);

	List<StudyProgramDto> toDtoList(List<StudyProgram> studyPrograms);

	@Named("toSummaryDto")
	@Mapping(target = "courses", ignore = true)
	StudyProgramDto toSummaryDto(StudyProgram studyProgram);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "faculty", ignore = true)
	@Mapping(target = "courses", ignore = true)
	StudyProgram toEntity(StudyProgramCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "faculty", ignore = true)
	@Mapping(target = "courses", ignore = true)
	void updateEntityFromDto(StudyProgramUpdateDto dto, @MappingTarget StudyProgram entity);

	default String courseToCourseCode(Course course) {
		return course.getCourseCode();
	}
}
