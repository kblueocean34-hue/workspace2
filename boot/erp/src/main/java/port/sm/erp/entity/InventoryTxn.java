package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "INV_STOCK_TXN",
        indexes = {
                @Index(name = "IDX_TXN_ITEM", columnList = "ITEM_ID"),
                @Index(name = "IDX_TXN_AT", columnList = "TXN_AT"),
        }
)
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class InventoryTxn {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INV_STOCK_TXN_SEQ_GEN")
    @SequenceGenerator(
            name = "INV_STOCK_TXN_SEQ_GEN", sequenceName = "SEQ_INV_STOCK_TXN",
            allocationSize = 1
    )
    @Column (name = "ID", columnDefinition = "NUMBER(19)") //NUMBER(19) → 19자리 숫자까지 저장 가능
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(name = "TXN_TYPE", nullable = false, length = 20)
    private StockTxnType txnType;

    @Column(name = "QTY", nullable = false, columnDefinition = "NUMBER(19)")
    private Long qty;

    @Column(name = "TXN_AT", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime txnAt;

    @Column(name = "REF_TYPE", length = 30)
    private String refType;

    @Column(name = "REF_ID", columnDefinition = "NUMBER(19)")
    private Long refId;

    @Column(name = "REMARK", length = 500)
    private String remark;


}
