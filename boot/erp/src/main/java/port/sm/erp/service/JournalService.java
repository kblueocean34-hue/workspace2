package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.JournalLineResponseDTO;
import port.sm.erp.dto.JournalResponseDTO;
import port.sm.erp.entity.*;
import port.sm.erp.repository.JournalRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JournalService {

    private final JournalRepository journalRepository;

    /** 목록 */
    @Transactional(readOnly = true)
    public Page<JournalResponseDTO> list(int page, int size, String q) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return journalRepository.findAll(pageable).map(this::toDto);
    }

    /** 단건 */
    @Transactional(readOnly = true)
    public JournalResponseDTO get(Long id) {
        Journal j = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("전표 없음"));
        return toDto(j);
    }

    /** 등록 */
    public JournalResponseDTO create(Journal journal) {

        if (journal.getLines() != null) {
            for (JournalLine line : journal.getLines()) {
                line.setId(null);
                line.setJournal(journal);
            }
        }

        Journal saved = journalRepository.save(journal);
        return toDto(saved);
    }

    /** 수정 */
    public JournalResponseDTO update(Long id, Journal req) {

        Journal j = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("전표 없음"));

        j.setJournalNo(req.getJournalNo());
        j.setJournalDate(req.getJournalDate());
        j.setCustomer(req.getCustomer());
        j.setRemark(req.getRemark());
        j.setStatus(req.getStatus());

        j.getLines().clear();
        if (req.getLines() != null) {
            for (JournalLine line : req.getLines()) {
                line.setId(null);
                line.setJournal(j);
                j.getLines().add(line);
            }
        }

        return toDto(j);
    }

    /** 삭제 */
    public void delete(Long id) {
        journalRepository.deleteById(id);
    }

    // ===============================
    // Entity -> DTO 변환
    // ===============================
    private JournalResponseDTO toDto(Journal j) {

        List<JournalLineResponseDTO> lines = j.getLines().stream()
                .map(l -> JournalLineResponseDTO.builder()
                        .id(l.getId())
                        .accountCode(l.getAccountCode())
                        .dcType(l.getDcType() == null ? null : l.getDcType().name())
                        .amount(l.getAmount())
                        .lineRemark(l.getLineRemark())
                        .build())
                .toList();

        return JournalResponseDTO.builder()
                .id(j.getId())
                .journalNo(j.getJournalNo())
                .journalDate(j.getJournalDate())
                .customerId(
                        j.getCustomer() == null ? null : j.getCustomer().getId()
                )
                .customerName(
                        j.getCustomer() == null ? null : j.getCustomer().getCustomerName()
                )
                .remark(j.getRemark())
                .status(j.getStatus() == null ? null : j.getStatus().name())
                .lines(lines)
                .build();
    }
}
