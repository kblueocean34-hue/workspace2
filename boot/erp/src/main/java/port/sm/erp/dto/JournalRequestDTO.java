package port.sm.erp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // ✅ customerName 같은 추가필드 무시
public class JournalRequestDTO {
    private String journalDate;      // "YYYY-MM-DD"
    private String journalNo;
    private String remark;
    private String status;           // "DRAFT"/"POSTED"
    private Long customerId;
    private Long totalAmount;        // null이면 백엔드에서 라인합으로 계산
    private List<JournalLineRequestDTO> lines;
}
