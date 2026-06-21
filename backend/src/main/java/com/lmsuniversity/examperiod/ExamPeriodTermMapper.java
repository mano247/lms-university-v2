package com.lmsuniversity.examperiod;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExamPeriodTermMapper {

	@Mapping(target = "registeredCount", ignore = true)
	ExamPeriodTermDto toDto(ExamPeriodTerm term);

	List<ExamPeriodTermDto> toDtoList(List<ExamPeriodTerm> terms);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "examPeriod", ignore = true)
	ExamPeriodTerm toEntity(ExamPeriodTermCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "examPeriod", ignore = true)
	void updateEntityFromDto(ExamPeriodTermUpdateDto dto, @MappingTarget ExamPeriodTerm entity);
}
