package com.samsung.mes.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsung.mes.member.dto.PurchaseMaterialRequest;
import com.samsung.mes.member.dto.PurchaseMaterialResponse;
import com.samsung.mes.member.service.PurchaseMaterialService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase/materials")
@CrossOrigin(origins = "http://localhost:5173")
public class PurchaseMaterialController {

    private final PurchaseMaterialService service;

    @GetMapping
    public Page<PurchaseMaterialResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.list(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public PurchaseMaterialResponse getOne(@PathVariable Long id) {
        return service.getOne(id);
    }

    @PostMapping
    public PurchaseMaterialResponse create(@Valid @RequestBody PurchaseMaterialRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public PurchaseMaterialResponse update(@PathVariable Long id, @Valid @RequestBody PurchaseMaterialRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
