package com.lmsuniversity.rectorate;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { RectorateMapper.class })
public interface UniversityMapper {

	@Mapping(target = "rectorate", source = "rectorate", qualifiedByName = "toSummaryDto")
	UniversityDto toDto(University university);

	List<UniversityDto> toDtoList(List<University> universities);

	@Named("toUniversitySummaryDto")
	@Mapping(target = "rectorate", ignore = true)
	UniversityDto toSummaryDto(University university);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "faculties", ignore = true)
	@Mapping(target = "teachers", ignore = true)
	@Mapping(target = "rectorate", ignore = true)
	University toEntity(UniversityCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "faculties", ignore = true)
	@Mapping(target = "teachers", ignore = true)
	@Mapping(target = "rectorate", ignore = true)
	void updateEntityFromDto(UniversityUpdateDto dto, @MappingTarget University entity);
}
