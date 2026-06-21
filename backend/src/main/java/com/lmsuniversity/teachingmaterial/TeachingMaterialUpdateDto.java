package com.lmsuniversity.teachingmaterial;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachingMaterialUpdateDto {
	@NotBlank
	private String title;
	private String authors;
	private String publicationYear;
	private String publisher;
	private String description;
	private String url;

	@Min(0)
	private int pageCount;

	@Min(0)
	private int quantity;

	@Min(0)
	private int issuedQuantity;

	private Outcome outcome;
	private Long courseId;
}
