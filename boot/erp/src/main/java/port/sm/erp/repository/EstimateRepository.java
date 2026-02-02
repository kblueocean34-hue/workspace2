package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.Estimate;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
}
