package com.lmsuniversity.officesupply;

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
public class OfficeSupplyUpdateDto {
	@NotBlank
	private String name;

	@Min(0)
	private int quantity;

	@Min(0)
	private int issuedQuantity;
}
