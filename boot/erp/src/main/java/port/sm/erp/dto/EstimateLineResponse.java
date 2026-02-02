package port.sm.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EstimateLineResponse {
    private Long id;
    private String itemName;
    private Integer qty;
    private BigDecimal price;
    private BigDecimal amount;
    private String remark;
}
