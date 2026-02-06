package port.sm.erp.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "MEMBERS") // Oracle에서는 MEMBER보다는 복수형 권장
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Member {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    @SequenceGenerator(name = "member_seq", sequenceName = "MEMBER_SEQ", allocationSize = 1)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "TEL")
    private String tel;

    @Column(name = "GENDER", nullable = false)
    private String gender;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private java.sql.Date createdAt;

    @Column(name = "UPDATED_AT", insertable = false, updatable = false)
    private java.sql.Date updatedAt;

    //추가
    @Column(name = "PROVIDER")
    private String provider;

    @Column(name = "PROVIDER_ID")
    private String providerId;

    @Column(name = "USERNAME")
    private String username;






}
