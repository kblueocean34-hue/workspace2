package com.samsung.mes.member.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

SalesOrder saved = repo.save(//DB저장
	SalesOrder.builder()
		.orderDate(req.getOrderDate())
		.customerCode(req.getCustomerCode())
		.customerName(req.getCustomerName())
		.itemCode(req.getItemCode())
		.itemName(req.getItemName())
		.orderQty(req.getOrderQty())
		.price(req.getPrice())
		.amount(amount)
		.deliveryDate(req.getDeliveryDate())
		.remark(req.getRemark())
		.build()
	);
	return toResponse(saved);//응답 객체 생성
}
    
//목록조회
public List<SalesOrderResponse> list(LocalDate from, LocalDate to) {
	
	if(from == null && to == null) {
		return repo.findAllByOrderByOrderDateDesc().stream().map(this::toResponse).toList();
	}
	
	//일부만 들어오면 기본값 보정
    LocalDate today = LocalDate.now();
    if (to == null) to = today;
    if (from == null) from = today.minusDays(30);

    return repo.findByOrderDateBetweenOrderByOrderDateDesc(from, to)
               .stream()
               .map(this::toResponse)
               .toList();
	
	/*LocalDate today = LocalDate.now();
	
	if (to == null) to = today;
	if (from == null) from = today.minusDays(30);
	//기간이 없으면 최근 30일
	
	return repo.findByOrderDateBetweenOrderByOrderDateDesc(from, to)
	//기간 사이의 수주 목록을 날짜 내림차순으로 가져옴
	
	.stream()
	.map(this::toResponse) //엔티티 -> 응답 DTO
	.toList();*/
}

	//단건조회
	public SalesOrderResponse get(Long id) {
		//id로 db검색 없으면 404오류 있으면 응답 dto로 반환
SalesOrder so = repo.findById(id)
.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "수주가 없습니다"));
return toResponse(so);
	}

	//삭제 db검색 없으면 404오류 있으면 삭제
public void delete(Long id) {
SalesOrder so = repo.findById(id)
.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "수주가 없습니다"));
repo.delete(so);	
}
//Entity -> Response변환 
private SalesOrderResponse toResponse(SalesOrder e) {
return SalesOrderResponse.builder()	
 .id(e.getId())
 .orderDate(e.getOrderDate())
 .customerCode(e.getCustomerCode())
 .customerName(e.getCustomerName())
 .itemCode(e.getItemCode())
 .itemName(e.getItemName())
 .orderQty(e.getOrderQty())
 .price(e.getPrice())
 .amount(e.getAmount())
 .deliveryDate(e.getDeliveryDate())		
 .remark(e.getRemark())		
 .build();				
 	}
	 
}    
//요청 -> 검증 -> 계산 -> db저장 -> 응답변환 -> 반환
//실제 기업 실무에서 쓰는 표준 패턴









