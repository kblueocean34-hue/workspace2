package com.samsung.mes.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samsung.mes.member.dto.ProductionOrderDTO;
import com.samsung.mes.member.service.ProductionOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/production/orders")
@RequiredArgsConstructor
public class ProductionOrderController {
	
	private final ProductionOrderService service;
	
	@GetMapping
	public List<ProductionOrderDTO> getOrders(){
		return service.getAllOrders();
	}
	
	@PostMapping
	public ProductionOrderDTO createOrder(@RequestBody ProductionOrderDTO dto) {
		return service.createOrder(dto);
	}
	
	
	
	
	
	
	
	
	
	

}
