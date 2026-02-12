package port.sm.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @Builder @AllArgsConstructor
public class NoticeDetailResponse {
    private Long id, viewCount;
    private String title, content, writer;
    private Boolean isPinned;
    private LocalDateTime createdAt, updatedAt;
}
