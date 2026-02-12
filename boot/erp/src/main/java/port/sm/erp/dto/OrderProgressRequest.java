package port.sm.erp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProgressRequest {

    private String orderNo;
    private String orderName;
    private String progressText;
    private Long memberId;   // FK는 ID만 받는게 정석
}
