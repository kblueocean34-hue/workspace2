package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.JournalResponseDTO;
import port.sm.erp.entity.Journal;
import port.sm.erp.service.JournalService;

@RestController
@RequestMapping("/api/acc/journals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 필요 없으면 지워도 됨
public class JournalController {

    private final JournalService journalService;

    /** 목록 조회 (검색/페이징) */
    @GetMapping
    public Page<JournalResponseDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String q
    ) {
        return journalService.list(page, size, q);
    }

    /** 단건 조회 */
    @GetMapping("/{id}")
    public JournalResponseDTO get(@PathVariable Long id) {
        return journalService.get(id);
    }

    /** 등록 */
    @PostMapping
    public JournalResponseDTO create(@RequestBody Journal journal) {
        return journalService.create(journal);
    }

    /** 수정 */
    @PutMapping("/{id}")
    public JournalResponseDTO update(
            @PathVariable Long id,
            @RequestBody Journal journal
    ) {
        return journalService.update(id, journal);
    }

    /** 삭제 */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        journalService.delete(id);
    }
}
