package port.sm.erp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalLineRequestDTO {
    private String accountCode;
    private String accountName;
    private String dcType;
    private Long amount;
    private String lineRemark;
}
