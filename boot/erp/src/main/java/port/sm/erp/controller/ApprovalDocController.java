package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.ApprovalDocRequestDTO;
import port.sm.erp.dto.ApprovalDocResponseDTO;
import port.sm.erp.service.ApprovalDocService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/approval")
public class ApprovalDocController {

    private final ApprovalDocService approvalDocService;

    // ✅ 전체 목록
    @GetMapping("/docs")
    public List<ApprovalDocResponseDTO> list() {
        return approvalDocService.list();
    }

    // ✅ 내 기안문서 (프론트: /api/approval/my-drafts?drafterId=1)
    @GetMapping("/my-drafts")
    public List<ApprovalDocResponseDTO> myDrafts(@RequestParam(required = false) Long drafterId) {
        if (drafterId == null) return approvalDocService.list(); // or 빈 리스트
        return approvalDocService.myDrafts(drafterId);
    }

    // ✅ 단건 상세
    @GetMapping("/docs/{id}")
    public ApprovalDocResponseDTO detail(@PathVariable Long id){
        return approvalDocService.detail(id);
    }

    // ✅ 신규 작성
    @PostMapping("/docs")
    public ApprovalDocResponseDTO create(@RequestBody ApprovalDocRequestDTO dto) {
        return approvalDocService.create(dto);
    }

    // ✅ 수정
    @PutMapping("/docs/{id}")
    public ApprovalDocResponseDTO update(@PathVariable Long id, @RequestBody ApprovalDocRequestDTO dto) {
        return approvalDocService.update(id, dto);
    }

    // ✅ 삭제
    @DeleteMapping("/docs/{id}")
    public void delete(@PathVariable Long id) {
        approvalDocService.delete(id);
    }
}
