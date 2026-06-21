package com.lmsuniversity.examperiod;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lmsuniversity.course.CourseMapper;
import com.lmsuniversity.user.RegisteredUserMapper;

@Mapper(componentModel = "spring", uses = { CourseMapper.class, RegisteredUserMapper.class, ExamPeriodTermMapper.class })
public interface ExamPeriodMapper {

	@Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toListDto")
	ExamPeriodDto toDto(ExamPeriod examPeriod);

	List<ExamPeriodDto> toDtoList(List<ExamPeriod> examPeriods);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "terms", ignore = true)
	ExamPeriod toEntity(ExamPeriodCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "terms", ignore = true)
	void updateEntityFromDto(ExamPeriodUpdateDto dto, @MappingTarget ExamPeriod entity);
}
