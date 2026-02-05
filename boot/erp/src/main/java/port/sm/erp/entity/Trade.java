package port.sm.erp.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TRADES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trade_seq")
    @SequenceGenerator(name = "trade_seq", sequenceName = "TRADE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TRADE_NO", nullable = false, unique = true)
    private String tradeNo;

    @Column(name = "TRADE_DATE", nullable = false)
    private String tradeDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRADE_TYPE", nullable = false)
    private TradeType tradeType;

    @Column(name = "SUPPLY_AMOUNT")
    private double supplyAmount;

    @Column(name = "VAT_AMOUNT")
    private double vatAmount;

    @Column(name = "FEE_AMOUNT")
    private double feeAmount;

    @Column(name = "TOTAL_AMOUNT")
    private double totalAmount;

    @Column(name = "REVENUE_ACCOUNT_CODE")
    private String revenueAccountCode;

    @Column(name = "EXPENSE_ACCOUNT_CODE")
    private String expenseAccountCode;

    @Column(name = "COUNTER_ACCOUNT_CODE")
    private String counterAccountCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private JournalStatus status;

    @Column(name = "DEPT_CODE")
    private String deptCode;

    @Column(name = "DEPT_NAME")
    private String deptName;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "PROJECT_NAME")
    private String projectName;

    @Column(name = "REMARK")
    private String remark;

    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalLine> lines;
}
