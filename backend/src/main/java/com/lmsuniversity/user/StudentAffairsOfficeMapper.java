package com.lmsuniversity.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lmsuniversity.faculty.FacultyMapper;

@Mapper(componentModel = "spring", uses = { FacultyMapper.class })
public interface StudentAffairsOfficeMapper {

	@Mapping(target = "userType", expression = "java(studentAffairsOffice.getClass().getSimpleName())")
	StudentAffairsOfficeDto toDto(StudentAffairsOffice studentAffairsOffice);

	List<StudentAffairsOfficeDto> toDtoList(List<StudentAffairsOffice> studentAffairsOffices);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "faculty", ignore = true)
	StudentAffairsOffice toEntity(StudentAffairsOfficeCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "faculty", ignore = true)
	void updateEntityFromDto(StudentAffairsOfficeUpdateDto dto, @MappingTarget StudentAffairsOffice entity);
}
