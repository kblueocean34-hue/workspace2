package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.TradeRequestDTO;
import port.sm.erp.dto.TradeResponseDTO;
import port.sm.erp.service.TradeService;

import java.util.List;

@RestController
@RequestMapping("/api/acc/trades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TradeController {

    private final TradeService tradeService;

    @PostMapping
    public TradeResponseDTO create(@RequestBody TradeRequestDTO request) {
        return tradeService.createTrade(request);
    }

    @GetMapping("/{id}")
    public TradeResponseDTO get(@PathVariable Long id) {
        return tradeService.getTradeById(id); // ✅ 이 이름으로 맞춤
    }

    @GetMapping
    public List<TradeResponseDTO> list() {
        return tradeService.getAllTrades(); // ✅ 이 이름으로 맞춤
    }

    @PutMapping("/{id}")
    public TradeResponseDTO update(@PathVariable Long id, @RequestBody TradeRequestDTO request) {
        return tradeService.updateTrade(id, request); // ✅ 이 이름으로 맞춤
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tradeService.deleteTrade(id); // ✅ 이 이름으로 맞춤
    }
}
