package com.lmsuniversity.officesupply;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OfficeSupplyMapper {

	OfficeSupplyDto toDto(OfficeSupply officeSupply);

	List<OfficeSupplyDto> toDtoList(List<OfficeSupply> officeSupplies);

	@Mapping(target = "id", ignore = true)
	OfficeSupply toEntity(OfficeSupplyCreateDto dto);

	@Mapping(target = "id", ignore = true)
	void updateEntityFromDto(OfficeSupplyUpdateDto dto, @MappingTarget OfficeSupply entity);
}
