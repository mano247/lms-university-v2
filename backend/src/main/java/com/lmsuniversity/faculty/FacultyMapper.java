package com.lmsuniversity.faculty;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lmsuniversity.rectorate.UniversityMapper;
import com.lmsuniversity.user.TeacherMapper;

@Mapper(componentModel = "spring", uses = { UniversityMapper.class, TeacherMapper.class })
public interface FacultyMapper {

	@Mapping(target = "university", source = "university", qualifiedByName = "toUniversitySummaryDto")
	FacultyDto toDto(Faculty faculty);

	List<FacultyDto> toDtoList(List<Faculty> faculties);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "university", ignore = true)
	@Mapping(target = "studyPrograms", ignore = true)
	@Mapping(target = "image", ignore = true)
	@Mapping(target = "dean", ignore = true)
	Faculty toEntity(FacultyCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "university", ignore = true)
	@Mapping(target = "studyPrograms", ignore = true)
	@Mapping(target = "image", ignore = true)
	@Mapping(target = "dean", ignore = true)
	void updateEntityFromDto(FacultyUpdateDto dto, @MappingTarget Faculty entity);
}
