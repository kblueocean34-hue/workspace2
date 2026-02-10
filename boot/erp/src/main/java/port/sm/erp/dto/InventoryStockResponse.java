package port.sm.erp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Builder
public class InventoryStockResponse {

    private Long id, itemId, onHandQty, reservedQty, availableQty, safetyQty;
    private String itemCode, itemName;
    private LocalDate lastMovedAt;
    private LocalDateTime createdAt, updatedAt;
}
