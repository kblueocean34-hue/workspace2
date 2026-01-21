package com.samsung.mes.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsung.mes.member.entity.ProductionOrder;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {

	boolean existsByWorkOrderNo(String workOrderNo);
}
