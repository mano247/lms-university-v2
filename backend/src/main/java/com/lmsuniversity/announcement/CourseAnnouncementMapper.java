package com.lmsuniversity.announcement;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lmsuniversity.course.CourseMapper;

@Mapper(componentModel = "spring", uses = { CourseMapper.class })
public interface CourseAnnouncementMapper {

	@Mapping(target = "imageUrl", source = "image")
	CourseAnnouncementDto toDto(CourseAnnouncement courseAnnouncement);

	List<CourseAnnouncementDto> toDtoList(List<CourseAnnouncement> courseAnnouncements);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "date", ignore = true)
	@Mapping(target = "course", ignore = true)
	CourseAnnouncement toEntity(CourseAnnouncementCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "date", ignore = true)
	@Mapping(target = "image", ignore = true)
	@Mapping(target = "course", ignore = true)
	void updateEntityFromDto(CourseAnnouncementUpdateDto dto, @MappingTarget CourseAnnouncement entity);
}
