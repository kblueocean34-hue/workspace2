package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.ApprovalDocRequestDTO;
import port.sm.erp.dto.ApprovalDocResponseDTO;
import port.sm.erp.entity.ApprovalDoc;
import port.sm.erp.entity.ApprovalStatus;
import port.sm.erp.entity.Member;
import port.sm.erp.repository.ApprovalDocRepository;
import port.sm.erp.repository.MemberRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApprovalDocService {

    private final ApprovalDocRepository approvalDocRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<ApprovalDocResponseDTO> list() {
        return approvalDocRepository.findList()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ApprovalDocResponseDTO> myDrafts(Long drafterId) {
        return approvalDocRepository.findMyDrafts(drafterId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ApprovalDocResponseDTO detail(Long id) {
        ApprovalDoc d = approvalDocRepository.findDetail(id);
        if (d == null) throw new IllegalArgumentException("기안 없음: " + id);
        return toResponse(d);
    }

    public ApprovalDocResponseDTO create(ApprovalDocRequestDTO dto) {
        ApprovalDoc d = new ApprovalDoc();
        applyDto(d, dto);

        ApprovalDoc saved = approvalDocRepository.save(d);

        // ✅ detail로 다시 조회( drafter/approver fetch join 포함 )
        ApprovalDoc full = approvalDocRepository.findDetail(saved.getId());
        if (full == null) throw new IllegalArgumentException("기안 없음: " + saved.getId());

        return toResponse(full);
    }

    public ApprovalDocResponseDTO update(Long id, ApprovalDocRequestDTO dto) {
        ApprovalDoc d = approvalDocRepository.findDetail(id);
        if (d == null) throw new IllegalArgumentException("기안없음: " + id);

        applyDto(d, dto);

        ApprovalDoc saved = approvalDocRepository.save(d);

        ApprovalDoc full = approvalDocRepository.findDetail(saved.getId());
        if (full == null) throw new IllegalArgumentException("기안 없음: " + saved.getId());

        return toResponse(full);
    }

    public void delete(Long id) {
        approvalDocRepository.deleteById(id);
    }

    // =========================
    // 내부 헬퍼
    // =========================
    private void applyDto(ApprovalDoc d, ApprovalDocRequestDTO dto) {

        // ✅ draftDate (LocalDate) - 여기서 빨간줄 해결
        // dto.getDraftDate()가 "2026-02-11" 형식이라고 가정
        if (dto.getDraftDate() != null && !dto.getDraftDate().trim().isEmpty()) {
            d.setDraftDate(LocalDate.parse(dto.getDraftDate().trim()));
        } else {
            // 엔티티 @PrePersist가 LocalDate.now()로 채워주지만,
            // 수정(update)에서도 안전하게 기본값 유지 원하면 이 줄 유지
            if (d.getDraftDate() == null) d.setDraftDate(LocalDate.now());
        }

        d.setTitle(requireText(dto.getTitle(), "title"));
        d.setContent(dto.getContent());

        // status (비어있으면 DRAFT)
        ApprovalStatus st = parseStatus(dto.getStatus());
        d.setStatus(st != null ? st : ApprovalStatus.DRAFT);

        // drafter/approver FK
        if (dto.getDrafterId() == null) throw new IllegalArgumentException("drafterId 필수");
        if (dto.getApproverId() == null) throw new IllegalArgumentException("approverId 필수");

        Member drafter = memberRepository.findById(dto.getDrafterId())
                .orElseThrow(() -> new IllegalArgumentException("기안자(Member) 없음: " + dto.getDrafterId()));
        Member approver = memberRepository.findById(dto.getApproverId())
                .orElseThrow(() -> new IllegalArgumentException("결재자(Member) 없음: " + dto.getApproverId()));

        d.setDrafter(drafter);
        d.setApprover(approver);

        // createdAt/updatedAt (엔티티 @PrePersist/@PreUpdate 있어도 OK)
        LocalDateTime now = LocalDateTime.now();
        if (d.getCreatedAt() == null) d.setCreatedAt(now);
        d.setUpdatedAt(now);
    }

    private ApprovalDocResponseDTO toResponse(ApprovalDoc d) {
        return ApprovalDocResponseDTO.builder()
                .id(d.getId())
                .draftDate(d.getDraftDate() != null ? d.getDraftDate().toString() : null)
                .title(d.getTitle())
                .content(d.getContent())
                .status(d.getStatus() != null ? d.getStatus().name() : null)

                .drafterId(d.getDrafter() != null ? d.getDrafter().getId() : null)
                .drafterName(d.getDrafter() != null ? safeMemberName(d.getDrafter()) : null)

                .approverId(d.getApprover() != null ? d.getApprover().getId() : null)
                .approverName(d.getApprover() != null ? safeMemberName(d.getApprover()) : null)

                .createdAt(d.getCreatedAt() != null ? d.getCreatedAt().toString() : null)
                .updatedAt(d.getUpdatedAt() != null ? d.getUpdatedAt().toString() : null)
                .build();
    }

    private String safeMemberName(Member m) {
        if (m == null) return null;

        // 1) username 우선
        String username = m.getUsername();
        if (username != null && !username.trim().isEmpty()) return username.trim();

        // 2) lastName + firstName
        String lastName = m.getLastName() == null ? "" : m.getLastName();
        String firstName = m.getFirstName() == null ? "" : m.getFirstName();
        String fullName = (lastName + firstName).trim();
        if (!fullName.isEmpty()) return fullName;

        // 3) email
        String email = m.getEmail();
        if (email != null && !email.trim().isEmpty()) return email.trim();

        // 4) fallback: id
        return String.valueOf(m.getId());
    }


    private static String requireText(String v, String field) {
        if (v == null) throw new IllegalArgumentException(field + " 필수");
        String s = v.trim();
        if (s.isEmpty()) throw new IllegalArgumentException(field + " 필수");
        return s;
    }

    private static ApprovalStatus parseStatus(String v) {
        if (v == null || v.trim().isEmpty()) return null;
        return ApprovalStatus.valueOf(v.trim());
    }



}
