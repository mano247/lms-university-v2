package com.lmsuniversity.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdministratorMapper {

	@Mapping(target = "userType", expression = "java(administrator.getClass().getSimpleName())")
	AdministratorDto toDto(Administrator administrator);

	List<AdministratorDto> toDtoList(List<Administrator> administrators);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	Administrator toEntity(AdministratorCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	void updateEntityFromDto(AdministratorUpdateDto dto, @MappingTarget Administrator entity);
}
