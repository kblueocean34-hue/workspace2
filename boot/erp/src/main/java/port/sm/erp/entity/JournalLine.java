package port.sm.erp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "JOURNAL_LINES")
public class JournalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "journal_line_seq")
    @SequenceGenerator(
            name = "journal_line_seq",
            sequenceName = "SEQ_JOURNAL_LINE",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "ACCOUNT_CODE", nullable = false, length = 50)
    private String accountCode;

    @Column(name = "ACCOUNT_NAME", length = 100)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(name = "DC_TYPE", nullable = false, length = 10)
    private DcType dcType;

    @Column(name = "AMOUNT", nullable = false)
    private Long amount;

    @Column(name = "LINE_REMARK", length = 255)
    private String lineRemark;

    /** 전표 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOURNAL_ID")
    private Journal journal;

    /** 거래 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRADE_ID")
    private Trade trade;

    public JournalLine() {}
}

