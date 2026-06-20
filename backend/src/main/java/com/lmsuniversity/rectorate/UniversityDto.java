package com.lmsuniversity.rectorate;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityDto {
	private Long id;
	private String name;
	private LocalDateTime foundingDate;
	private String contact;
	private String rector;
	private String address;
	private String description;
	private String image;
	private RectorateDto rectorate;
}
