package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.TradeRequestDTO;
import port.sm.erp.dto.TradeResponseDTO;
import port.sm.erp.service.TradeService;

import java.util.List;

@RestController
@RequestMapping("/api/sales/sales")
@RequiredArgsConstructor
public class SalesController {

    private final TradeService tradeService;

    @GetMapping
    public ResponseEntity<List<TradeResponseDTO>> list() {
        return ResponseEntity.ok(tradeService.getAllTrades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeResponseDTO> detail(@PathVariable Long id) {
        return ResponseEntity.ok(tradeService.getTradeById(id));
    }

    @PostMapping
    public ResponseEntity<TradeResponseDTO> create(@RequestBody TradeRequestDTO dto) {
        return ResponseEntity.ok(tradeService.createTrade(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeResponseDTO> update(@PathVariable Long id, @RequestBody TradeRequestDTO dto) {
        return ResponseEntity.ok(tradeService.updateTrade(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tradeService.deleteTrade(id);
        return ResponseEntity.ok().build();
    }
}
