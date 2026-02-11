package port.sm.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ApprovalDocResponseDTO {

    private String draftDate, title, content, status, drafterName, approverName, createdAt, updatedAt;
    private Long drafterId, approverId, id;
}
