package port.sm.erp.dto;

import lombok.*;


@Data
public class ApprovalDocRequestDTO {

    private String draftDate, title, content, status;
    private Long drafterId, approverId, id;
}
