package port.sm.erp.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalLineResponseDTO {
    private Long id;
    private String accountCode;
    private String accountName;   // ✅ 이게 있어야 accountName() 가능
    private String dcType;
    private Long amount;
    private String lineRemark;
}
