package com.samsung.mes.member.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samsung.mes.member.entity.SalesOrder;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

	//기간 조회
List<SalesOrder> findByOrderDateBetweenOrderByOrderDateDesc(LocalDate from, LocalDate to);
List<SalesOrder> findAllByOrderByOrderDateDesc();
}
