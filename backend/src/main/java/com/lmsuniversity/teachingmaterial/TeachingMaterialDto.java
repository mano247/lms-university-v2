package com.lmsuniversity.teachingmaterial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachingMaterialDto {

	private Long id;
	private String title;
	private String authors;
	private String publicationYear;
	private String publisher;
	private String description;
	private String url;
	private int pageCount;
	private int quantity;
	private int issuedQuantity;
	private Outcome outcome;
}
