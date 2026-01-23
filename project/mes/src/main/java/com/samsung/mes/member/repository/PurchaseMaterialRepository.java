package com.samsung.mes.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samsung.mes.member.entity.PurchaseMaterial;

public interface PurchaseMaterialRepository extends JpaRepository<PurchaseMaterial, Long> {
    boolean existsByPurchaseNo(String purchaseNo);
}
