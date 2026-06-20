package com.lmsuniversity.officesupply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeSupplyDto {

    private Long id;
	private String name;
	private int quantity;
	private int issuedQuantity;
}
