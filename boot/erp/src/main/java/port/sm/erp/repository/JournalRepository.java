package port.sm.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.Journal;

public interface JournalRepository extends JpaRepository<Journal, Long> {

    Page<Journal> findByJournalNoContainingOrRemarkContaining(
            String journalNo,
            String remark,
            Pageable pageable
    );
}
