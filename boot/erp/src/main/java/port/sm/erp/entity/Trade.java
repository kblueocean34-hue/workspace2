package port.sm.erp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TRADES")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trade_seq")
    @SequenceGenerator(name = "trade_seq", sequenceName = "TRADE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    // ⚠️ unique + not null이면 create 시 반드시 값이 있어야 함
    @Column(name = "TRADE_NO", nullable = false, unique = true, length = 50)
    private String tradeNo;

    /**
     * ✅ 지금 DB가 VARCHAR2이거나, 기존 데이터 포맷이 날짜로 변환 불가해서 터지는 상태임
     * 그래서 우선 String으로 둬서 "2026-02-06" 같은 값 그대로 저장/조회되게 함
     * (나중에 DB 컬럼을 DATE로 마이그레이션하면 LocalDate로 다시 바꾸면 됨)
     */
    @Column(name = "TRADE_DATE", nullable = false, length = 20)
    private String tradeDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRADE_TYPE", nullable = false, length = 20)
    private TradeType tradeType;

    @Column(name = "SUPPLY_AMOUNT", nullable = false)
    private double supplyAmount;

    @Column(name = "VAT_AMOUNT", nullable = false)
    private double vatAmount;

    @Column(name = "FEE_AMOUNT", nullable = false)
    private double feeAmount;

    @Column(name = "TOTAL_AMOUNT", nullable = false)
    private double totalAmount;

    @Column(name = "REVENUE_ACCOUNT_CODE", length = 20)
    private String revenueAccountCode;

    @Column(name = "EXPENSE_ACCOUNT_CODE", length = 20)
    private String expenseAccountCode;

    @Column(name = "COUNTER_ACCOUNT_CODE", length = 20)
    private String counterAccountCode;

    // ✅ 핵심: 컬럼명 명시 + 기본값 + not null
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    @Builder.Default
    private TradeStatus status = TradeStatus.DRAFT;

    @Column(name = "DEPT_CODE", length = 50)
    private String deptCode;

    @Column(name = "DEPT_NAME", length = 100)
    private String deptName;

    @Column(name = "PROJECT_CODE", length = 50)
    private String projectCode;

    @Column(name = "PROJECT_NAME", length = 100)
    private String projectName;

    @Column(name = "REMARK", length = 500)
    private String remark;

    //필드 새로추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "MEMBER_ID")
    private Member user;

    // ✅ 양방향 관계면 직렬화 무한루프 방지 필요
    @JsonManagedReference
    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<JournalLine> lines = new ArrayList<>();

    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    //cascade = CascadeType.ALL “Trade 저장/삭제할 때 TradeLine도 같이 처리해라” 라는 뜻.
    //orphanRemoval = true 부모(Trade)에서 자식(TradeLine)을 컬렉션에서 빼면, DB에서도 삭제해라”
    private java.util.List<TradeLine> tradeLines = new java.util.ArrayList<>();
    //TradeLine을 담는 리스트를 null이 아닌 상태로 항상 초기화해두는 코드.
    public void addTradeLine(TradeLine line){
        this.tradeLines.add(line);
        line.setTrade(this);
    }
}
