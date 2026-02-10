package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.StockMovementResponse;
import port.sm.erp.service.StockMovementService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stock")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @GetMapping("/movement")
    public List<StockMovementResponse> movement(@RequestParam(required = false) String q) {
        return stockMovementService.list(q);
    }
}
