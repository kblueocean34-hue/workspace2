package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.InventoryStockRequest;
import port.sm.erp.dto.InventoryStockResponse;
import port.sm.erp.service.InventoryStockService;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class InventoryStockController {

    private final InventoryStockService stockService;

    @GetMapping
    public ResponseEntity<Page<InventoryStockResponse>> list(
            Pageable pageable,
            @RequestParam(required = false) String q
    ) {// 페이징 정보와 검색어(q) 받기
        return ResponseEntity.ok(stockService.list(pageable, q));
        //서비스 호출 후 200 OK 응답 반환
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryStockResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.get(id));
    }

    //신규
    @PostMapping
    public ResponseEntity<InventoryStockResponse> create(@RequestBody InventoryStockRequest req){
        return ResponseEntity.ok(stockService.create(req));
    }

    //수정
    @PutMapping("/{id}")
    public ResponseEntity<InventoryStockResponse> update(@PathVariable Long id,
         @RequestBody InventoryStockRequest req){
        return ResponseEntity.ok(stockService.update(id, req));
    }

    //삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
