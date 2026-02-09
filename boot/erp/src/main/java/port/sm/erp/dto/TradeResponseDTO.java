package port.sm.erp.dto;

import lombok.Data;
import port.sm.erp.entity.Trade;

import java.util.List;
import java.util.stream.Collectors;

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

    // ✅ 추가 필드
    private Long customerId;
    private String customerName;

    private Long userId; // (선택)

    private List<TradeLineResponseDTO> tradeLines;

    public TradeResponseDTO(Trade trade) {
        this.id = trade.getId();
        this.tradeNo = trade.getTradeNo();
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

        // =========================
        // ✅ 거래처 매핑
        // =========================
        if (trade.getCustomer() != null) {
            this.customerId = trade.getCustomer().getId();
            this.customerName = trade.getCustomer().getCustomerName();
        }

        // =========================
        // ✅ 사용자 매핑 (있을 때만)
        // =========================
        if (trade.getUser() != null) {
            // Member PK가 id면 이거
            this.userId = trade.getUser().getId();

            // Member PK가 memberId면 이걸로 바꿔
            // this.userId = trade.getUser().getMemberId();
        }

        // =========================
        // ✅ 거래 라인 매핑
        // =========================
        if (trade.getTradeLines() != null) {
            this.tradeLines = trade.getTradeLines()
                    .stream()
                    .map(TradeLineResponseDTO::new)
                    .collect(Collectors.toList());
        }
    }
}
