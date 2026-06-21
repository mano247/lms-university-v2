package com.lmsuniversity.schedule;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lmsuniversity.course.CourseMapper;
import com.lmsuniversity.user.TeacherMapper;

@Mapper(componentModel = "spring", uses = { CourseMapper.class, TeacherMapper.class })
public interface ClassScheduleMapper {

	ClassScheduleDto toDto(ClassSchedule classSchedule);

	List<ClassScheduleDto> toDtoList(List<ClassSchedule> classSchedules);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course", ignore = true)
	@Mapping(target = "teacher", ignore = true)
	ClassSchedule toEntity(ClassScheduleCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course", ignore = true)
	@Mapping(target = "teacher", ignore = true)
	void updateEntityFromDto(ClassScheduleUpdateDto dto, @MappingTarget ClassSchedule entity);
}
