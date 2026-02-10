package port.sm.erp.dto;

import lombok.*;
import javax.validation.constraints.*;

@Getter @Setter
public class InventoryStockRequest {
    @NotNull(message = "품목 ID는 필수 입니다")
    private Long itemId;
    
    @Min(value = 0, message = "보유수량은 0 이상이어야 합니다" )
    private Long onHandQty;

    @Min(value = 0, message = "예약수량은 0 이상이어야 합니다" )
    private Long reservedQty;

    @Min(value = 0, message = "안전재고는 0 이상이어야 합니다" )
    private Long safetyQty;
    
}
