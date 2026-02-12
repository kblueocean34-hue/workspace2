package port.sm.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @Builder @AllArgsConstructor
public class NoticeListResponse {
    private Long id, viewCount;
    private String title, writer;
    private Boolean isPinned;
    private LocalDateTime createdAt;
}
