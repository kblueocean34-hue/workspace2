package com.samsung.mes.member.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.samsung.mes.member.dto.SalesOrderRequest;
import com.samsung.mes.member.dto.SalesOrderResponse;
import com.samsung.mes.member.entity.SalesOrder;
import com.samsung.mes.member.repository.SalesOrderRepository;
import com.samsung.mes.security.RequestValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service//서비스(업무로직담당)
@RequiredArgsConstructor//생성자 자동생성
@Transactional//DB작업을 한 묶음으로 처리 중간에 오류가 나면 롤백(전부취소)되어 db가 꼬이지 않게 보호
public class SalesOrderService {//수주등록/조회/삭제 같은 비즈니스 로직을 모아둔 서비스
	
	private final SalesOrderRepository repo;

	//SalesOrderRequest req 프론트에서 보낸 
	//“수주 등록 정보” (orderDate, customerCode, itemCode, qty, price …)
	
	//출력: SalesOrderResponse DB 저장 후 결과(저장된 id 포함)를 다시 프론트에 돌려주기 위한 DTO
    public SalesOrderResponse create(SalesOrderRequest req) {
		RequestValidator.validate(req);//이 요청이 정상인지 검사
/*orderDate가 null인지 customerCode가 비었는지 orderQty가 0 이하인지 price가 null 또는 음수인지*/
//잘못된 값은 빨리 막아야 데이터 깔끔
//금액계산 (수량 x 단가)		
BigDecimal amount = req.getPrice().multiply(BigDecimal.valueOf(req.getOrderQty()));	

SalesOrder saved = repo.save(
SalesOrder.builder()
.orderDate(req.getOrderDate())
.customerCode(req.getCustomerCode())
.customerName(req.getCustomerName())
.itemCode(req.getItemCode())
);
		
		
		
		
		
		
		
		
		
	}

}


