package port.sm.erp.dto;

import lombok.Data;

import java.util.List;

@Data
public class TradeLineResponseDTO {
    private Long id;

    private Long itemId;
    private String itemName;

    private Long qty;
    private Long unitPrice;

    private Long supplyAmount;
    private Long vatAmount;
    private Long totalAmount;

    private String remark;

}
