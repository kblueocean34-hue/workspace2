package port.sm.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class JournalResponse {

    private Long id;
    private LocalDate journalDate;
    private List<JournalLineResponseDTO> lines;
}
