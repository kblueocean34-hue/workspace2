package port.sm.erp.dto;

import lombok.Data;
import port.sm.erp.entity.Trade;

@Data
public class TradeResponseDTO {

    private Long id;
    private String tradeNo;
    private String tradeDate;   // 엔티티도 String
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

    public TradeResponseDTO(Trade trade) {
        this.id = trade.getId();
        this.tradeNo = trade.getTradeNo();

        // ✅ 엔티티가 String이므로 그대로 대입
        this.tradeDate = trade.getTradeDate();

        this.tradeType = trade.getTradeType() != null
                ? trade.getTradeType().name()
                : null;

        this.supplyAmount = trade.getSupplyAmount();
        this.vatAmount = trade.getVatAmount();
        this.feeAmount = trade.getFeeAmount();
        this.totalAmount = trade.getTotalAmount();

        this.revenueAccountCode = trade.getRevenueAccountCode();
        this.expenseAccountCode = trade.getExpenseAccountCode();
        this.counterAccountCode = trade.getCounterAccountCode();

        this.deptCode = trade.getDeptCode();
        this.deptName = trade.getDeptName();
        this.projectCode = trade.getProjectCode();
        this.projectName = trade.getProjectName();

        this.remark = trade.getRemark();

        this.status = trade.getStatus() != null
                ? trade.getStatus().name()
                : null;
    }
}
