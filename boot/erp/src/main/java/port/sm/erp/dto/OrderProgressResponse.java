package port.sm.erp.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderProgressResponse {

    private Long id;
    private String orderNo;
    private String orderName;
    private String progressText;
    private String status;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
