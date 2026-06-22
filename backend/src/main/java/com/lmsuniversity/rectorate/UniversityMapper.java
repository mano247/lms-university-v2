package com.lmsuniversity.rectorate;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UniversityMapper {

	@Mapping(target = "rectorate", source = "rectorate", qualifiedByName = "toRectorateSummaryDto")
	UniversityDto toDto(University university);

	List<UniversityDto> toDtoList(List<University> universities);

	@Named("toRectorateSummaryDto")
	static RectorateDto toRectorateSummaryDto(Rectorate rectorate) {
		if (rectorate == null) {
			return null;
		}
		return RectorateDto.builder()
				.id(rectorate.getId())
				.name(rectorate.getName())
				.contact(rectorate.getContact())
				.image(rectorate.getImage())
				.address(rectorate.getAddress())
				.rectorName(rectorate.getRectorName())
				.build();
	}

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
