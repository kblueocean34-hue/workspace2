package com.samsung.mes.member.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderResponse {
	
	private Long id;
	private LocalDate orderDate;
	
	private String customerCode;
	private String customerName;
	
	private String itemCode;
	private String itemName;
	
	private Long orderQty;
	private BigDecimal price;
	private BigDecimal amount;
	
	private LocalDate deliveryDate;
	private String remark;
	

}
