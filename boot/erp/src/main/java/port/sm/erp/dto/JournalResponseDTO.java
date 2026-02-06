package port.sm.erp.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalResponseDTO {

    private Long id;
    private String journalNo;
    private LocalDate journalDate;

    private Long customerId;
    private String customerName;

    private String remark;
    private String status;

    private List<JournalLineResponseDTO> lines;
}
