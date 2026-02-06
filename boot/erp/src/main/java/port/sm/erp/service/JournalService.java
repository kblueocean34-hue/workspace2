package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.*;
import port.sm.erp.entity.*;
import port.sm.erp.repository.CustomerRepository;
import port.sm.erp.repository.JournalRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JournalService {

    private final JournalRepository journalRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Page<JournalResponseDTO> list(int page, int size, String q) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Journal> result = (q == null || q.isBlank())
                ? journalRepository.findAll(pageable)
                : journalRepository.findByJournalNoContainingOrRemarkContaining(q, q, pageable);

        return result.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public JournalResponseDTO get(Long id) {
        Journal j = journalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("전표 없음 id=" + id));
        return toResponse(j);
    }

    /** 등록: 부모 먼저 저장/flush로 id 확보 → 그 다음 라인 추가 */
    public JournalResponseDTO create(JournalRequestDTO req) {
        if (req == null) throw new IllegalArgumentException("요청값이 비었습니다.");

        Journal j = new Journal();

        // ✅ 절대 id 세팅 금지 (DB가 identity always면 특히)
        j.setId(null);

        applyHeaderOnly(j, req);

        // ✅ 부모 먼저 저장 + flush
        journalRepository.saveAndFlush(j);

        // ✅ 여기서 id가 null이면: JPA/DB id 생성 전략이 깨진 것 (설정 문제)
        if (j.getId() == null) {
            throw new IllegalStateException("Journal ID가 생성되지 않았습니다. (DB IDENTITY/SEQUENCE 설정 또는 Hibernate Dialect 확인 필요)");
        }

        applyLinesOnly(j, req);

        // ✅ 라인 포함 flush
        journalRepository.saveAndFlush(j);

        return toResponse(j);
    }

    /** 수정: 기존 엔티티에 대해 header/lines 재구성 */
    public JournalResponseDTO update(Long id, JournalRequestDTO req) {
        if (req == null) throw new IllegalArgumentException("요청값이 비었습니다.");

        Journal j = journalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("전표 없음 id=" + id));

        applyHeaderOnly(j, req);
        applyLinesOnly(j, req);

        journalRepository.saveAndFlush(j);

        return toResponse(j);
    }

    public void delete(Long id) {
        journalRepository.deleteById(id);
    }

    private void applyHeaderOnly(Journal j, JournalRequestDTO req) {
        j.setJournalNo(req.getJournalNo());

        if (req.getJournalDate() != null && !req.getJournalDate().isBlank()) {
            j.setJournalDate(LocalDate.parse(req.getJournalDate().trim()));
        } else if (j.getJournalDate() == null) {
            j.setJournalDate(LocalDate.now());
        }

        j.setRemark(req.getRemark());

        String st = (req.getStatus() == null || req.getStatus().isBlank())
                ? "DRAFT"
                : req.getStatus().trim().toUpperCase();
        j.setStatus(JournalStatus.valueOf(st));

        if (req.getCustomerId() != null) {
            Customer c = customerRepository.findById(req.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("거래처 없음 customerId=" + req.getCustomerId()));
            j.setCustomer(c);
        } else {
            j.setCustomer(null);
        }
    }

    /** 라인은 무조건 addLine()로 추가 (양방향 보장) */
    private void applyLinesOnly(Journal j, JournalRequestDTO req) {
        if (req.getLines() == null || req.getLines().isEmpty()) {
            throw new IllegalArgumentException("전표 라인이 비어있습니다.");
        }

        j.getLines().clear(); // orphanRemoval=true

        for (JournalLineRequestDTO l : req.getLines()) {
            if (l == null) continue;

            if (l.getAccountCode() == null || l.getAccountCode().isBlank()) {
                throw new IllegalArgumentException("계정코드(accountCode)가 비어있습니다.");
            }

            String dc = (l.getDcType() == null) ? "" : l.getDcType().trim().toUpperCase();
            if (!dc.equals("DEBIT") && !dc.equals("CREDIT")) {
                throw new IllegalArgumentException("dcType 값이 잘못되었습니다: " + l.getDcType());
            }

            Long amt = (l.getAmount() == null) ? 0L : l.getAmount();
            if (amt <= 0) {
                throw new IllegalArgumentException("amount는 0보다 커야 합니다. amount=" + amt);
            }

            JournalLine line = new JournalLine();
            line.setAccountCode(l.getAccountCode().trim());
            line.setAccountName(l.getAccountName());
            line.setDcType(DcType.valueOf(dc));
            line.setAmount(amt);
            line.setLineRemark(l.getLineRemark());

            // 일반전표: trade 없음
            line.setTrade(null);

            // ✅ 핵심: 양방향 세팅 보장
            j.addLine(line);
        }
    }

    private JournalResponseDTO toResponse(Journal j) {
        List<JournalLineResponseDTO> lines = j.getLines().stream()
                .map(l -> JournalLineResponseDTO.builder()
                        .id(l.getId())
                        .accountCode(l.getAccountCode())
                        .accountName(l.getAccountName())
                        .dcType(l.getDcType() == null ? null : l.getDcType().name())
                        .amount(l.getAmount())
                        .lineRemark(l.getLineRemark())
                        .build()
                )
                .collect(Collectors.toList());

        return JournalResponseDTO.builder()
                .id(j.getId())
                .journalNo(j.getJournalNo())
                .journalDate(j.getJournalDate() == null ? null : j.getJournalDate().toString())
                .customerId(j.getCustomer() == null ? null : j.getCustomer().getId())
                .customerName(j.getCustomer() == null ? null : j.getCustomer().getCustomerName())
                .remark(j.getRemark())
                .status(j.getStatus() == null ? null : j.getStatus().name())
                .lines(lines)
                .build();
    }
}
