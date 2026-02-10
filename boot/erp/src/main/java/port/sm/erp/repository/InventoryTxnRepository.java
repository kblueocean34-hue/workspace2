package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.InventoryTxn;

public interface InventoryTxnRepository extends JpaRepository<InventoryTxn, Long> {
}
