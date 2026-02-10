package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.Trade;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    Trade findByTradeNo(String tradeNo);
    List<Trade> findByStatus(String status);
}
