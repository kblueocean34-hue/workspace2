// MemberMeResponseDTO.java
package port.sm.erp.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberMeResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
