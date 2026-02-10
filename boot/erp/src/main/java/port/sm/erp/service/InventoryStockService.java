package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.repository.InventoryStockRepository;
import port.sm.erp.repository.ItemRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryStockService {

    private final InventoryStockRepository stockRepository;
    private final ItemRepository itemRepository;
}
