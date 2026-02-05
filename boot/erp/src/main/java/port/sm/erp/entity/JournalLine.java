package port.sm.erp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/*Journal의 자식(상세) 엔티티입니다.
전표 1건
├─ 차변 | 현금 | 1,000,000
└─ 대변 | 매출 | 1,000,000
* */

@Entity //DB에 저장되는 객체
@Table(name = "JOURNAL_LINES")
@Getter
@Setter
public class JournalLine {

    @Id //기본키(PK) – 전표 라인 번호
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "journal_line_seq")
    @SequenceGenerator(
        name = "journal_line_seq",
        sequenceName = "SEQ_JOURNAL_LINE",
        allocationSize = 1
    )
    private Long id;

    //계정과목과 코드  & 이름
    private String accountCode; //101. 401
    private String accountName; //현금, 매출

    //차변 DEBIT / 대변 CREDIT - Enum
    @Enumerated(EnumType.STRING)
    private DcType dcType;

    //금액 전표라인의 금액 정수기반(원단위) 소수점 오류 방지
    private Long amount;

    /**적요*/
    private String lineRemark;

    //전표와의 관계 (핵심)
    @ManyToOne(fetch = FetchType.LAZY)
    //@ManyToOne 여러 개의 JournalLine 하나의 Journal ✔ 전표 : 라인 = 1 : N
    //fetch = FetchType.LAZY 👉 필요할 때만 전표를 가져옴 📌 성능 최적화 필수 옵션

    @JoinColumn(name = "JOURNAL_ID") //👉 외래키(FK) 컬럼
    private Journal journal;

    //전표와의 관계 (핵심)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRADE_ID", nullable = false) //👉 외래키(FK) 컬럼
    private Trade trade;



}
