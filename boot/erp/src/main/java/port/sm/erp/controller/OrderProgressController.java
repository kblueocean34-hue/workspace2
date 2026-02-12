package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.entity.OrderProgress;
import port.sm.erp.service.OrderProgressService;

import java.util.List;

@RestController
@RequestMapping("/api/orders/progress")
@RequiredArgsConstructor
public class OrderProgressController {

    private final OrderProgressService orderProgressService;

    /**
     * 등록
     */
    @PostMapping
    public ResponseEntity<OrderProgress> create(@RequestBody OrderProgress request) {
        return ResponseEntity.ok(orderProgressService.create(request));
    }

    /**
     * ID로 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderProgress> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderProgressService.getById(id));
    }

    /**
     * 오더번호로 조회 (단건)
     */
    @GetMapping("/order-no/{orderNo}")
    public ResponseEntity<OrderProgress> getByOrderNo(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderProgressService.getByOrderNo(orderNo));
    }

    /**
     * 오더명 LIKE 검색 (리스트)
     */
    @GetMapping("/search")
    public ResponseEntity<List<OrderProgress>> searchByOrderName(
            @RequestParam String keyword) {
        return ResponseEntity.ok(orderProgressService.searchByOrderName(keyword));
    }

    /**
     * 오더명 LIKE 검색 (페이징)
     */
    @GetMapping("/search/page")
    public ResponseEntity<Page<OrderProgress>> searchByOrderNamePage(
            @RequestParam String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(orderProgressService.searchByOrderName(keyword, pageable));
    }

    /**
     * 상태값 조회
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderProgress>> getByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(orderProgressService.getByStatus(status));
    }

    /**
     * 작성자 기준 조회
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<OrderProgress>> getByMemberId(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(orderProgressService.getByMemberId(memberId));
    }

    /**
     * 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody OrderProgress request) {

        orderProgressService.update(id, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 삭제 (Soft Delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderProgressService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<OrderProgress>> getAll() {
        return ResponseEntity.ok(
                orderProgressService.getByStatus("ACTIVE")
        );
    }
}
