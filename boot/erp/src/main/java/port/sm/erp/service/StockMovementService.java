package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.StockMovementResponse;
import port.sm.erp.entity.*;
import port.sm.erp.repository.InventoryStockRepository;
import port.sm.erp.repository.InventoryTxnRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockMovementService {

    private final InventoryStockRepository stockRepository; // INV_STOCK (현재수량)
    private final InventoryTxnRepository txnRepository;     // INV_STOCK_TXN (입/출고 내역)

    public List<StockMovementResponse> list(String q) {

        // 1) 재고(품목목록) 기반으로 행 만들기
        List<InventoryStock> stocks = stockRepository.findAll();

        // 검색
        if (q != null && !q.trim().isEmpty()) {
            String qq = q.trim().toLowerCase();
            stocks = stocks.stream()
                    .filter(s -> {
                        Item it = s.getItem();
                        if (it == null) return false;
                        String code = nz(it.getItemCode()).toLowerCase();
                        String name = nz(it.getItemName()).toLowerCase();
                        return code.contains(qq) || name.contains(qq);
                    })
                    .collect(Collectors.toList());
        }

        // 2) 트랜잭션 전체 로드 후 itemId 기준 집계
        List<InventoryTxn> txns = txnRepository.findAll();

        Map<Long, Long> inMap = new HashMap<>();
        Map<Long, Long> outMap = new HashMap<>();

        for (InventoryTxn t : txns) {
            if (t.getItem() == null || t.getItem().getId() == null) continue;
            Long itemId = t.getItem().getId();

            long qty = n(t.getQty());
            StockTxnType type = t.getTxnType();

            if (type == StockTxnType.IN) {
                inMap.put(itemId, inMap.getOrDefault(itemId, 0L) + qty);
            } else if (type == StockTxnType.OUT) {
                outMap.put(itemId, outMap.getOrDefault(itemId, 0L) + qty);
            }
        }

        // 3) 응답 생성
        List<StockMovementResponse> result = new ArrayList<>();

        for (InventoryStock s : stocks) {
            Item it = s.getItem();
            if (it == null || it.getId() == null) continue;

            Long itemId = it.getId();

            long startQty = n(s.getOnHandQty()); // ✅ 임시: 현재재고를 기초로
            long inQty = inMap.getOrDefault(itemId, 0L);
            long outQty = outMap.getOrDefault(itemId, 0L);
            long endQty = startQty + inQty - outQty;

            result.add(StockMovementResponse.builder()
                    .id(itemId)
                    .itemCode(it.getItemCode())
                    .itemName(it.getItemName())
                    .startQty(startQty)
                    .inQty(inQty)
                    .outQty(outQty)
                    .endQty(endQty)
                    .build());
        }

        return result;
    }

    private long n(Long v) {
        return v == null ? 0L : v;
    }

    private String nz(String v) {
        return v == null ? "" : v;
    }
}
