package com.samsung.mes.member.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

//json관련
import com.fasterxml.jackson.annotation.JsonFormat;

//what 엔티티 = DB테이블을 자바 클래스로 표현한것  1=1 클래스의 필드(변수) 1개 = 테이블의 컬럼 1개
//객체 1개 = 테이블의 row 1개
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity //이 클래스는 JPA가 관리하는 DB 테이블용 클래스야” 라고 알려줌
@Table(
	name = "sales_order",
	//실제 테이블 이름을 sales_order로 지정 그리고 인덱스(index) 도 같이 생성하겠다는 뜻
	//index  DB에서 검색 속도를 빠르게 해주는 “목차” 같은 것
	//예를 들어 수주 목록을 “날짜로 조회”, “거래처로 필터”, “품목으로 검색”을 자주 한다면 인덱스가 효과 큼
//인덱스는 너무 많이 만들면 “저장/수정” 성능이 약간 느려질 수 있다
	indexes = {
	@Index (name = "idx_sales_order_order_date", columnList = "order_date"),
	@Index (name = "idx_sales_order_customer_code", columnList = "customer_code"),
	@Index (name = "idx_sales_order_item_code", columnList = "item_code")
	}
		
)
@Getter//리턴
@Setter//세팅
@NoArgsConstructor//JPA는 기본 생성자가 필요해서 @NoArgsConstructor가 필수
@AllArgsConstructor
@Builder //객체를 만들때 편하게 작성가능
public class SalesOrder {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO_INCREMENT
private Long id;

@Column(name = "order_date", nullable = false)//값이 반듯이 있어야 함
@JsonFormat(pattern = "yyyy-MM-dd")
//프론트/백 통신(JSON)할때 "2026-01-09"형태로 주고 받게 해줌
//안 붙이면 날짜 포멧이 깨지거나 다르게 나오는 경우가 있다
private LocalDate orderDate;

//거래처 코드/명
@Column(name="customer_code", nullable=false, length=30)
private String customerCode;

@Column(name="customer_name", nullable=false, length=100)
private String customerName;

//품목 코드/명
@Column(name="item_code", nullable=false, length=30)
private String itemCode;

@Column(name="item_name", nullable=false, length=200)
private String itemName;

//수주수량 수량은 정수니까 0이라도 값을 넣어 줘야 합니다 null이면 저장불가
@Column(name="order_qty", nullable=false)
private Long orderQty;

@Column(name="price", nullable=false, precision=18, scale=2)
private BigDecimal price;//BigDecimal 돈계산에 안전
//why do 자리 =3자리면 자동 반올림 오류가 생김

@Column(name="amount", nullable=false, precision=18, scale=2)
private BigDecimal amount;

//남품 예정일
@Column(name = "delivery_date")
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate deliveryDate;

//비고
@Column(name = "remark", length = 1000)
private String remark;
}
