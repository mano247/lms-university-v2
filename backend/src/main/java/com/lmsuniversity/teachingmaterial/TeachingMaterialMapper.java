package com.lmsuniversity.teachingmaterial;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeachingMaterialMapper {

	TeachingMaterialDto toDto(TeachingMaterial teachingMaterial);

	List<TeachingMaterialDto> toDtoList(List<TeachingMaterial> teachingMaterials);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course", ignore = true)
	TeachingMaterial toEntity(TeachingMaterialCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course", ignore = true)
	void updateEntityFromDto(TeachingMaterialUpdateDto dto, @MappingTarget TeachingMaterial entity);
}
