package com.samsung.mes.member.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Getter
@Setter
public class SalesOrderRequest {

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate orderDate;
	private String customerCode;
	private String customerName;
	private String itemCode;
	private String itemName;
	private Long orderQty;
	private BigDecimal price;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate deliveryDate;
	private String remark;
	
}
