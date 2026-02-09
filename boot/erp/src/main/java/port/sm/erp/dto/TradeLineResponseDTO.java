package port.sm.erp.dto;

import lombok.Data;
import port.sm.erp.entity.TradeLine;

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

    // ★ 생성자 추가
    public TradeLineResponseDTO(TradeLine line) {
        this.id = line.getId();

        if (line.getItem() != null) {
            this.itemId = line.getItem().getId();
            this.itemName = line.getItem().getItemName();
        }

        this.qty = line.getQty();
        this.unitPrice = line.getUnitPrice();
        this.supplyAmount = line.getSupplyAmount();
        this.vatAmount = line.getVatAmount();
        this.totalAmount = line.getTotalAmount();
        this.remark = line.getRemark();
    }

}
