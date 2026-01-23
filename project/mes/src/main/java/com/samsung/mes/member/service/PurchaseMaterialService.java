package com.samsung.mes.member.service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsung.mes.member.dto.PurchaseMaterialRequest;
import com.samsung.mes.member.dto.PurchaseMaterialResponse;
import com.samsung.mes.member.entity.PurchaseMaterial;
import com.samsung.mes.member.repository.PurchaseMaterialRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseMaterialService {

    private final PurchaseMaterialRepository repo;

    public Page<PurchaseMaterialResponse> list(Pageable pageable) {
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );
        return repo.findAll(p).map(this::toRes);
    }

    public PurchaseMaterialResponse getOne(Long id) {
        PurchaseMaterial e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("구매자재가 없습니다. id=" + id));
        return toRes(e);
    }

    @Transactional
    public PurchaseMaterialResponse create(PurchaseMaterialRequest req) {
        if (repo.existsByPurchaseNo(req.getPurchaseNo())) {
            throw new IllegalArgumentException("이미 존재하는 구매번호입니다: " + req.getPurchaseNo());
        }

        BigDecimal amount = safeAmount(req.getQty(), req.getUnitPrice(), req.getAmount());

        PurchaseMaterial e = PurchaseMaterial.builder()
                .purchaseDate(req.getPurchaseDate())
                .purchaseNo(req.getPurchaseNo())
                .supplierCode(req.getSupplierCode())
                .supplierName(req.getSupplierName())
                .itemCode(req.getItemCode())
                .itemName(req.getItemName())
                .qty(req.getQty())
                .unitPrice(req.getUnitPrice())
                .amount(amount)
                .expectedDate(req.getExpectedDate())
                .status(req.getStatus() == null || req.getStatus().isBlank() ? "대기" : req.getStatus())
                .remark(req.getRemark() == null ? "" : req.getRemark())
                .build();

        return toRes(repo.save(e));
    }

    @Transactional
    public PurchaseMaterialResponse update(Long id, PurchaseMaterialRequest req) {
        PurchaseMaterial e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("구매자재가 없습니다. id=" + id));

        BigDecimal amount = safeAmount(req.getQty(), req.getUnitPrice(), req.getAmount());

        e.setPurchaseDate(req.getPurchaseDate());
        // 구매번호는 프론트에서 disabled라 보통 변경 금지 (정책상 유지)
        // e.setPurchaseNo(req.getPurchaseNo());

        e.setSupplierCode(req.getSupplierCode());
        e.setSupplierName(req.getSupplierName());
        e.setItemCode(req.getItemCode());
        e.setItemName(req.getItemName());
        e.setQty(req.getQty());
        e.setUnitPrice(req.getUnitPrice());
        e.setAmount(amount);
        e.setExpectedDate(req.getExpectedDate());
        e.setStatus(req.getStatus());
        e.setRemark(req.getRemark() == null ? "" : req.getRemark());

        return toRes(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("구매자재가 없습니다. id=" + id);
        }
        repo.deleteById(id);
    }

    private BigDecimal safeAmount(Integer qty, BigDecimal unitPrice, BigDecimal sentAmount) {
        BigDecimal q = BigDecimal.valueOf(qty == null ? 0 : qty);
        BigDecimal p = unitPrice == null ? BigDecimal.ZERO : unitPrice;
        BigDecimal calc = p.multiply(q);

        // 프론트가 준 amount가 0이거나 비었으면 계산값으로 대체
        if (sentAmount == null) return calc;
        if (sentAmount.compareTo(BigDecimal.ZERO) <= 0) return calc;
        return sentAmount;
    }

    private PurchaseMaterialResponse toRes(PurchaseMaterial e) {
        return PurchaseMaterialResponse.builder()
                .id(e.getId())
                .purchaseDate(e.getPurchaseDate())
                .purchaseNo(e.getPurchaseNo())
                .supplierCode(e.getSupplierCode())
                .supplierName(e.getSupplierName())
                .itemCode(e.getItemCode())
                .itemName(e.getItemName())
                .qty(e.getQty())
                .unitPrice(e.getUnitPrice())
                .amount(e.getAmount())
                .expectedDate(e.getExpectedDate())
                .status(e.getStatus())
                .remark(e.getRemark())
                .build();
    }
}