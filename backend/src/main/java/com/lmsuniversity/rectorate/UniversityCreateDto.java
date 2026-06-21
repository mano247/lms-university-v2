package com.lmsuniversity.rectorate;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityCreateDto {
	@NotBlank
	private String name;
	private LocalDateTime foundingDate;
	private String contact;
	private String description;
	private String image;
	private String address;

	@NotNull
	private Long rectorateId;
}
