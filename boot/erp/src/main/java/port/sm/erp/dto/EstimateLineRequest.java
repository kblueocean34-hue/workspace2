package port.sm.erp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EstimateLineRequest {


    private LocalDate estimateDate;
    private String customerName,remark, itemName;
    private Integer qty;
    private BigDecimal price;


}
