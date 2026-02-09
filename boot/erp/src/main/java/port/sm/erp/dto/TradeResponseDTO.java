package port.sm.erp.dto;

import lombok.Data;
import port.sm.erp.entity.Trade;

//추가
import java.util.List;
import java.util.stream.Collectors; // ★추가

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

    //필드추가
    private Long customerId;
    private String customerName;

    private Long userId; // (선택)

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

        // =========================
        // ★추가 : 거래처 매핑
        // =========================
        if (trade.getCustomer() != null) {
            this.customerId = trade.getCustomer().getId();
            this.customerName = trade.getCustomer().getCustomerName();
        }

        // =========================
        // ★추가 : 사용자 매핑 (있을 때만)
        // =========================
        if (trade.getUser() != null) {
            this.userId = trade.getUser().getId();
            this.customerName = trade.getCustomer().getCustomerName(); // 엔티티 필드명에 맞춰 수정
        }
        // ✅ 추가: 사용자
        if (trade.getUser() != null) {
            this.userId = trade.getUser().getId(); // getMemberId() 아니고 보통 getId()
        }


        // =========================
        // ★추가 : 거래 라인 매핑
        // =========================
        if (trade.getTradeLines() != null) {
            this.tradeLines = trade.getTradeLines()
                    .stream()
                    .map(TradeLineResponseDTO::new)
                    .collect(Collectors.toList());
        }



    }



    private List<TradeLineResponseDTO> tradeLines;

}
