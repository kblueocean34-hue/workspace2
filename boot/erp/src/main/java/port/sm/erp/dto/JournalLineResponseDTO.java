package port.sm.erp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalLineResponseDTO {

    private Long id;
    private String accountCode;
    private String dcType;
    private Long amount;
    private String lineRemark;
}
