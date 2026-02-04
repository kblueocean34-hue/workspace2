package port.sm.erp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import port.sm.erp.entity.CustomerType;

@Getter
@Setter
@NoArgsConstructor
public class CustomerRequest {
    private String customerCode;
    private String customerName;
    private String ceoName;
    private String phone;
    private String email;
    private String address;
    private CustomerType customerType;
    private String remark;
}
