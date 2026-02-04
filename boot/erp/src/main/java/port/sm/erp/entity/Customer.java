package port.sm.erp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMERS")  // 오라클에서 대문자로 테이블 이름 지정
@Getter
@Setter
@NoArgsConstructor //기본 생성자
@AllArgsConstructor //모든 필드를 받는 생성자
public class Customer {

    @Id //기본키
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")  // 오라클에서는 SEQUENCE 사용
    @SequenceGenerator(name = "customer_seq", sequenceName = "CUSTOMER_SEQ", allocationSize = 1) // 시퀀스 생성
    private Long id;

    // 일반 컬럼들
    @Column(length = 200, nullable = false)
    private String customerCode, customerName, ceoName, phone, email, address, remark, detailAddress;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
}
