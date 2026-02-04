package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.CustomerResponse;
import port.sm.erp.entity.Customer;
import port.sm.erp.service.CustomerService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // CORS 설정은 그대로 유지
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // 거래처 목록조회
    @GetMapping("/api/acc/customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();  // Service에서 처리

        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }

        List<CustomerResponse> customerResponses = customers.stream()
                .map(CustomerResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(customerResponses);
    }

    // 거래처 상세조회
    @GetMapping("/api/acc/customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 거래처 등록
    @PostMapping("/api/acc/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    // 거래처 수정
    @PutMapping("/api/acc/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return updatedCustomer != null ? ResponseEntity.ok(updatedCustomer) : ResponseEntity.notFound().build();
    }

    // 거래처 삭제
    @DeleteMapping("/api/acc/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
