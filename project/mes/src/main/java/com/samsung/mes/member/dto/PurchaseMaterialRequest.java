package com.samsung.mes.member.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseMaterialRequest {

    @NotNull
    private LocalDate purchaseDate;

    @NotBlank
    private String purchaseNo;

    @NotBlank
    private String supplierCode;

    @NotBlank
    private String supplierName;

    @NotBlank
    private String itemCode;

    @NotBlank
    private String itemName;

    @NotNull @Min(1)
    private Integer qty;

    @NotNull @DecimalMin("0.00")
    private BigDecimal unitPrice;

    @NotNull @DecimalMin("0.00")
    private BigDecimal amount;

    @NotNull
    private LocalDate expectedDate;

    @NotBlank
    private String status;

    private String remark;
}