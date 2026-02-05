package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.TradeRequestDTO;
import port.sm.erp.dto.TradeResponseDTO;
import port.sm.erp.service.TradeService;

import java.util.List;

@RestController
@RequestMapping("/api/acc/trades")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping
    public ResponseEntity<TradeResponseDTO> create(@RequestBody TradeRequestDTO request) {
     TradeResponseDTO saved = tradeService.createTrade(request);
     return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeResponseDTO> getOne(@PathVariable Long id) {
        TradeResponseDTO dto = tradeService.getAllTradeById(id);
        return ResponseEntity.ok(dto);
    }

    //전표전체
    @GetMapping
    public ResponseEntity<List<TradeResponseDTO>> getAll(){
        List<TradeResponseDTO> list = tradeService.getAllTrades();
        return ResponseEntity.ok(list);
    }

    //전표수정
    @PutMapping("/{id}")
    public ResponseEntity<TradeResponseDTO> update(
            @PathVariable Long id,
            @RequestBody TradeRequestDTO request
    ){
        TradeResponseDTO updated = tradeService.updateTrade(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tradeService.deleteTrade(id);
        return ResponseEntity.noContent().build();
    }

}
