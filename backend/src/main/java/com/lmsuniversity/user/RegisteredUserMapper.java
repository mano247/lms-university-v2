package com.lmsuniversity.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RegisteredUserMapper {

	@Mapping(target = "userType", expression = "java(user.getClass().getSimpleName())")
	@Mapping(target = "permission", source = "permissions")
	RegisteredUserDto toDto(RegisteredUser user);

	List<RegisteredUserDto> toDtoList(List<RegisteredUser> users);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	RegisteredUser toEntity(RegisteredUserCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	void updateEntityFromDto(RegisteredUserUpdateDto dto, @MappingTarget RegisteredUser entity);
}
