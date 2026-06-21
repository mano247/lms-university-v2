package com.lmsuniversity.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

	@Mapping(target = "userType", expression = "java(teacher.getClass().getSimpleName())")
	TeacherDto toDto(Teacher teacher);

	List<TeacherDto> toDtoList(List<Teacher> teachers);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "university", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "courses", ignore = true)
	@Mapping(target = "examAttempts", ignore = true)
	Teacher toEntity(TeacherCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "university", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "courses", ignore = true)
	@Mapping(target = "examAttempts", ignore = true)
	void updateEntityFromDto(TeacherUpdateDto dto, @MappingTarget Teacher entity);
}
