package port.sm.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import port.sm.erp.entity.DcType;

@Getter
@AllArgsConstructor
public class JournalLineResponse {

    private String accountCode;
    private String accountName;
    private DcType dcType;
    private Long amount;
}
