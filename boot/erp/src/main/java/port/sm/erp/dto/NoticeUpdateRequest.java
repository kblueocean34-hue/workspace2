package port.sm.erp.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class NoticeUpdateRequest {
    private String title, content;
    private Boolean isPinned;
}
