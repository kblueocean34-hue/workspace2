package port.sm.erp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalLineRequestDTO {
    private Long id;
    private String accountCode;
    private String accountName;
    private String dcType;   // ✅ "DEBIT" / "CREDIT"
    private Long amount;
    private String lineRemark;
}
