package port.sm.erp.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class StockMovementResponse {
    private Long id;          // itemId
    private String itemCode;
    private String itemName;

    private Long startQty;    // 기초재고(임시: 현재재고)
    private Long inQty;       // 입고합
    private Long outQty;      // 출고합
    private Long endQty;      // 기말재고 = start + in - out
}
