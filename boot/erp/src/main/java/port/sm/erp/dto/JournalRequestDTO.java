package port.sm.erp.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalRequestDTO {
    private String journalNo;
    private String journalDate;
    private Long customerId;
    private String remark;
    private String status;
    private List<JournalLineRequestDTO> lines;
}
