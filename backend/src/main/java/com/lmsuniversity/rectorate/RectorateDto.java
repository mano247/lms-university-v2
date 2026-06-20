package com.lmsuniversity.rectorate;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RectorateDto {

	private Long id;
	private String name;
	private String contact;
	private String image;
	private String address;
	private String rectorName;
	private Set<UniversityDto> universities;
}
