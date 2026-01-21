package com.samsung.mes.member.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "production_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionOrder {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK, 자동생성

    @Column(nullable = false)
    private String workOrderNo; // 지시번호 (고유)

    @Column(nullable = false)
    private LocalDate orderDate; // 지시일

    @Column(nullable = false)
    private String itemCode; // 품목코드

    @Column(nullable = false)
    private String itemName; // 품목명

    @Column(nullable = false)
    private Integer planQty; // 계획수량

    private LocalDate startDate; // 시작일
    private LocalDate endDate;   // 종료일

    @Column(nullable = false)
    private String status; // 상태: 대기 / 진행중 / 완료

    private String remark; // 비고
}
