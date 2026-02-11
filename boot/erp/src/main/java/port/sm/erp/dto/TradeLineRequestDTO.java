package port.sm.erp.dto;

import lombok.Data;

@Data
public class TradeLineRequestDTO {
    private Long itemId;
    private Long qty;
    private Long unitPrice;
    private Long supplyAmount;
    private Long vatAmount;
    private Long totalAmount;
    private String remark;
}
