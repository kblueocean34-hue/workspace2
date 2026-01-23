package com.samsung.mes.member.dto;

import java.math.BigDecimal;
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
public class PurchaseMaterialResponse {

    private Long id;

    private LocalDate purchaseDate;
    private String purchaseNo;

    private String supplierCode;
    private String supplierName;

    private String itemCode;
    private String itemName;

    private Integer qty;
    private BigDecimal unitPrice;
    private BigDecimal amount;

    private LocalDate expectedDate;
    private String status;
    private String remark;
}