package port.sm.erp.dto;

import lombok.Data;

@Data
public class TradeRequestDTO {
    private String tradeNo, tradeDate, tradeType, revenueAccountCode, expenseAccountCode,
            counterAccountCode, deptCode, deptName, projectCode, projectName, remark;
    private double supplyAmount, vatAmount, feeAmount, totalAmount;
}
