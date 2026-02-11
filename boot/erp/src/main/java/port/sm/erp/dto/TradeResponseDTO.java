package port.sm.erp.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TradeResponseDTO {

    private Long id;

    private String tradeNo;
    private String tradeDate;
    private String tradeType;

    private double supplyAmount;
    private double vatAmount;
    private double feeAmount;
    private double totalAmount;

    private String revenueAccountCode;
    private String expenseAccountCode;
    private String counterAccountCode;

    private String deptCode;
    private String deptName;
    private String projectCode;
    private String projectName;

    private String remark;
    private String status;

    private Long customerId;
    private String customerName;

    @Builder.Default
    private List<TradeLineResponseDTO> tradeLines = new ArrayList<>();
}
