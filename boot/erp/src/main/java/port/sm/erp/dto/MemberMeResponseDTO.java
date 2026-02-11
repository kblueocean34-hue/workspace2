package port.sm.erp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDTO {

    private String firstName,lastName,email,password,companyName,position,tel,address,detailAddress,gender; // 추가
}
