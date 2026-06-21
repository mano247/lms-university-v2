package com.lmsuniversity.announcement;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {

	@Mapping(target = "postedAt", source = "date")
	AnnouncementDto toDto(Announcement announcement);

	List<AnnouncementDto> toDtoList(List<Announcement> announcements);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "date", ignore = true)
	Announcement toEntity(AnnouncementCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "date", ignore = true)
	@Mapping(target = "image", ignore = true)
	void updateEntityFromDto(AnnouncementUpdateDto dto, @MappingTarget Announcement entity);
}
