package com.samsung.mes.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_material")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Column(nullable = false, unique = true, length = 50)
    private String purchaseNo;

    @Column(nullable = false, length = 50)
    private String supplierCode;

    @Column(nullable = false, length = 100)
    private String supplierName;

    @Column(nullable = false, length = 50)
    private String itemCode;

    @Column(nullable = false, length = 100)
    private String itemName;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate expectedDate;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "대기";

    @Column(length = 500)
    private String remark;
}
