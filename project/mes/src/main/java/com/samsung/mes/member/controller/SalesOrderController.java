package com.samsung.mes.member.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsung.mes.member.dto.SalesOrderRequest;
import com.samsung.mes.member.dto.SalesOrderResponse;
import com.samsung.mes.member.service.SalesOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales/orders")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SalesOrderController {
	
	private final SalesOrderService service;
	
	//수주등록
	@PostMapping
	public SalesOrderResponse create(@RequestBody SalesOrderRequest req) {
		return service.create(req);
	}
	
	
	//수주목록조회(기간)
	@GetMapping
	public Page<SalesOrderResponse> list(
@RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
@RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
//페이징
@RequestParam(defaultValue = "0") int page,
@RequestParam(defaultValue = "10") int size
			){
		return service.list(from, to, page, size);
	}
	
	
	//수주상세
	@GetMapping("/{id}")
	public SalesOrderResponse get(@PathVariable Long id) {
		return service.get(id);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
	
	
	
	
	
	

}
