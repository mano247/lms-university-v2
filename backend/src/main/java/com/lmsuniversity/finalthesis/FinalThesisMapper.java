package com.lmsuniversity.finalthesis;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lmsuniversity.user.StudentMapper;
import com.lmsuniversity.user.TeacherMapper;

@Mapper(componentModel = "spring", uses = { StudentMapper.class, TeacherMapper.class })
public interface FinalThesisMapper {

	@Mapping(target = "student", source = "student", qualifiedByName = "toSummaryDto")
	FinalThesisDto toDto(FinalThesis finalThesis);

	List<FinalThesisDto> toDtoList(List<FinalThesis> finalTheses);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "student", ignore = true)
	@Mapping(target = "mentor", ignore = true)
	FinalThesis toEntity(FinalThesisCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "student", ignore = true)
	@Mapping(target = "mentor", ignore = true)
	void updateEntityFromDto(FinalThesisUpdateDto dto, @MappingTarget FinalThesis entity);
}
