package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
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
    public List<TradeResponseDTO> list() {
        return tradeService.getAllTrades();
    }

    @GetMapping("/{id}")
    public TradeResponseDTO get(@PathVariable Long id) {
        return tradeService.getTradeById(id);
    }

    @PostMapping
    public TradeResponseDTO create(@RequestBody TradeRequestDTO dto) {
        return tradeService.createTrade(dto);
    }

    @PutMapping("/{id}")
    public TradeResponseDTO update(@PathVariable Long id, @RequestBody TradeRequestDTO dto) {
        return tradeService.updateTrade(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tradeService.deleteTrade(id);
    }
}
