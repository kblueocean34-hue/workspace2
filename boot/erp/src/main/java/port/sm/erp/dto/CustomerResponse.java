package port.sm.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import port.sm.erp.entity.Customer;
import port.sm.erp.entity.CustomerType;

@Getter
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String customerCode;
    private String customerName;
    private String ceoName;
    private String phone;
    private String email;
    private String address;
    private CustomerType customerType;
    private String remark;

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getCustomerCode(),
                customer.getCustomerName(),
                customer.getCeoName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getCustomerType(),
                customer.getRemark()
        );
    }
}
