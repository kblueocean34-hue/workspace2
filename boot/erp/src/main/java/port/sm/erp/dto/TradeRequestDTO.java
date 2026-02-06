package port.sm.erp.dto;

import lombok.Data;

@Data
public class TradeRequestDTO {
    private String tradeNo;
    private String tradeDate;
    private String tradeType;

    private String revenueAccountCode;
    private String expenseAccountCode;
    private String counterAccountCode;

    private String deptCode;
    private String deptName;
    private String projectCode;
    private String projectName;

    private String remark;

    private double supplyAmount;
    private double vatAmount;
    private double feeAmount;
    private double totalAmount;

    private String status; // optional
}
