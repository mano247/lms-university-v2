package com.lmsuniversity.rectorate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RectorateUpdateDto {
	@NotBlank
	private String name;
	private String contact;
	private String image;
	private String address;
	private String rectorName;
}
