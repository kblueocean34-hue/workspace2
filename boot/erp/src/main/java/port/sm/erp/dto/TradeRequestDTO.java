package port.sm.erp.dto;

import lombok.Data;

//add
import java.util.List;

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

    //필드추가
    private Long customerId;          // Trade.customer 저장용
    private Long userId;              // (선택) Trade.user 저장용 - 쓰면 추가

    private List<TradeLineRequestDTO> tradeLines;  // 라인들 클래스 새로만듬
}
