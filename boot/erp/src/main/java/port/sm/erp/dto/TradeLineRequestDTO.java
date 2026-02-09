package port.sm.erp.dto;

import lombok.Data;

@Data
public class TradeLineRequestDTO {
    private Long itemId;
    private Long qty;
    private Long unitPrice;
    private String remark; // 선택
}
