package port.sm.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class EstimateResponse {

    private Long id;
    private String estimateNo;
    private LocalDate estimateDate;
    private String customerName;
    private String remark;
    private List<EstimateLineResponse> lines;
}
