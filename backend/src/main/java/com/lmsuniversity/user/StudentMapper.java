package com.lmsuniversity.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lmsuniversity.faculty.FacultyMapper;

@Mapper(componentModel = "spring", uses = { FacultyMapper.class })
public interface StudentMapper {

	@Mapping(target = "userType", expression = "java(student.getClass().getSimpleName())")
	@Mapping(target = "permission", source = "permissions")
	StudentDto toDto(Student student);

	List<StudentDto> toDtoList(List<Student> students);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "faculty", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "studyYears", ignore = true)
	@Mapping(target = "examAttempts", ignore = true)
	@Mapping(target = "courses", ignore = true)
	Student toEntity(StudentCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "faculty", ignore = true)
	@Mapping(target = "studyYears", ignore = true)
	@Mapping(target = "examAttempts", ignore = true)
	@Mapping(target = "courses", ignore = true)
	void updateEntityFromDto(StudentUpdateDto dto, @MappingTarget Student entity);
}
