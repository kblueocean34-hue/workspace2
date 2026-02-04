package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
