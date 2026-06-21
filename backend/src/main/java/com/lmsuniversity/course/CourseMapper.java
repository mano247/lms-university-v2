package com.lmsuniversity.course;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lmsuniversity.studyprogram.StudyProgramMapper;
import com.lmsuniversity.teachingmaterial.TeachingMaterialDto;
import com.lmsuniversity.teachingmaterial.TeachingMaterial;
import com.lmsuniversity.user.TeacherMapper;

@Mapper(componentModel = "spring", uses = { StudyProgramMapper.class, TeacherMapper.class })
public interface CourseMapper {

	@Mapping(target = "studyProgram", source = "studyProgram", qualifiedByName = "toSummaryDto")
	CourseDto toDto(Course course);

	List<CourseDto> toDtoList(List<Course> courses);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "teacher", ignore = true)
	@Mapping(target = "studyProgram", ignore = true)
	@Mapping(target = "teachingMaterials", ignore = true)
	@Mapping(target = "examAttempts", ignore = true)
	@Mapping(target = "students", ignore = true)
	@Mapping(target = "announcements", ignore = true)
	Course toEntity(CourseCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "teacher", ignore = true)
	@Mapping(target = "studyProgram", ignore = true)
	@Mapping(target = "teachingMaterials", ignore = true)
	@Mapping(target = "examAttempts", ignore = true)
	@Mapping(target = "students", ignore = true)
	@Mapping(target = "announcements", ignore = true)
	void updateEntityFromDto(CourseUpdateDto dto, @MappingTarget Course entity);

	TeachingMaterialDto teachingMaterialToDto(TeachingMaterial teachingMaterial);
}
