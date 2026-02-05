package port.sm.erp.dto;

import lombok.Data;
import port.sm.erp.entity.Trade;

@Data
public class TradeResponseDTO {
    private String tradeNo, tradeDate, tradeType, revenueAccountCode, expenseAccountCode,
            counterAccountCode, deptCode, deptName, projectCode, projectName, remark;
    private double supplyAmount, vatAmount, feeAmount, totalAmount;

    // Trade 객체를 받는 생성자 추가
    public TradeResponseDTO(Trade trade) {
        this.tradeNo = trade.getTradeNo();
        this.tradeDate = trade.getTradeDate();
        this.tradeType = trade.getTradeType().toString(); // enum을 String으로 변환
        this.revenueAccountCode = trade.getRevenueAccountCode();
        this.expenseAccountCode = trade.getExpenseAccountCode();
        this.counterAccountCode = trade.getCounterAccountCode();
        this.deptCode = trade.getDeptCode();
        this.deptName = trade.getDeptName();
        this.projectCode = trade.getProjectCode();
        this.projectName = trade.getProjectName();
        this.remark = trade.getRemark();
        this.supplyAmount = trade.getSupplyAmount();
        this.vatAmount = trade.getVatAmount();
        this.feeAmount = trade.getFeeAmount();
        this.totalAmount = trade.getTotalAmount();
    }
}
