package com.samsung.mes.member.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class ProductionOrderDTO {
	
private Long id;
private Integer planQty;
private String workOrderNo, itemCode, itemName, status, remark;
private LocalDate orderDate, startDate, endDate;

}
