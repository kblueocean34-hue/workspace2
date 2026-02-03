package com.hbk.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDTO {
    private String	firstName,lastName,email,password,repeatPassword,companyName,position,tel,address,detailAddress,gender;
}
