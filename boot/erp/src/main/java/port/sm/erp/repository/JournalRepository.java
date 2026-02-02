package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.Journal;

public interface JournalRepository extends JpaRepository<Journal, Long> {
}
