package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.TradeResponseDTO;
import port.sm.erp.service.TradeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/acc/trades") // 너 프론트가 쓰는 경로랑 맞춤
public class TradeController {

    private final TradeService tradeService;

    @GetMapping
    public List<TradeResponseDTO> list() {
        return tradeService.list();
    }

    @GetMapping("/{id}")
    public TradeResponseDTO detail(@PathVariable Long id) {
        return tradeService.getDetail(id); // ✅ 이제 빨간불 절대 안뜸
    }
}
