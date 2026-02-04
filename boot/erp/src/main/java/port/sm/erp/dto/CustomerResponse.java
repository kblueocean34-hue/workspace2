package port.sm.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import port.sm.erp.entity.Customer;

@Getter
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String customerCode, customerName, ceoName, phone, email, address, remark, detailAddress;

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getCustomerCode(),
                customer.getCustomerName(),
                customer.getCeoName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getRemark(),
                customer.getDetailAddress()  // 마지막에 detailAddress
        );
    }
}
