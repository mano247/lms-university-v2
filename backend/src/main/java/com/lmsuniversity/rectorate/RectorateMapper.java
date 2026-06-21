package com.lmsuniversity.rectorate;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { UniversityMapper.class })
public interface RectorateMapper {

	@Mapping(target = "universities", source = "universities", qualifiedByName = "toUniversitySummaryDto")
	RectorateDto toDto(Rectorate rectorate);

	List<RectorateDto> toDtoList(List<Rectorate> rectorates);

	@Named("toSummaryDto")
	@Mapping(target = "universities", ignore = true)
	RectorateDto toSummaryDto(Rectorate rectorate);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "universities", ignore = true)
	Rectorate toEntity(RectorateCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "universities", ignore = true)
	void updateEntityFromDto(RectorateUpdateDto dto, @MappingTarget Rectorate entity);
}
