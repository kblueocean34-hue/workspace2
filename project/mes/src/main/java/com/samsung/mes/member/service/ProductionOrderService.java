package com.samsung.mes.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.samsung.mes.member.dto.ProductionOrderDTO;
import com.samsung.mes.member.entity.ProductionOrder;
import com.samsung.mes.member.repository.ProductionOrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductionOrderService {
	
private final ProductionOrderRepository repository;//Repository 주입 (DB 접근)
//👉 이 객체로 DB 조회 / 저장을 합니다.

public List<ProductionOrderDTO> getAllOrders(){//전체 생산지시 조회
 return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
//:: -> 메서드 참조 (Method Reference)
 /*
return repository.findAll()   // 1️⃣ DB에서 전체 조회
.stream()             // 2️⃣ 리스트를 하나씩 처리
.map(this::toDto)     // 3️⃣ Entity → DTO 변환
.collect(Collectors.toList()); // 4️⃣ 다시 리스트로 
 */
}
	
public ProductionOrderDTO createOrder(ProductionOrderDTO dto) {
	if(repository.existsByWorkOrderNo(dto.getWorkOrderNo())) {
		throw new RuntimeException("이미 존재하는 지시번호 입니다");
	}
	ProductionOrder entity = ProductionOrder.builder() //2️⃣ DTO → Entity 변환
			.workOrderNo(dto.getWorkOrderNo())
			.orderDate(dto.getOrderDate())
			.itemCode(dto.getItemCode())
			.planQty(dto.getPlanQty())
			.startDate(dto.getStartDate())
			.endDate(dto.getEndDate())
			.status(dto.getStatus() != null ? dto.getStatus() : "대기")
			//👉 상태가 없으면 기본값 "대기"
			.remark(dto.getRemark())
			.build();
			ProductionOrder saved = repository.save(entity);			
			return toDto(saved);
}
	
private ProductionOrderDTO toDto(ProductionOrder entity) {
	return ProductionOrderDTO.builder()
.id(entity.getId())
.workOrderNo(entity.getWorkOrderNo())
.orderDate(entity.getOrderDate())
.itemCode(entity.getItemCode())
.itemName(entity.getItemName())
.planQty(entity.getPlanQty())
.startDate(entity.getStartDate())
.endDate(entity.getEndDate())
.status(entity.getStatus())
.remark(entity.getRemark())
.build();
}
	
	
	
	
	
	
	

}
