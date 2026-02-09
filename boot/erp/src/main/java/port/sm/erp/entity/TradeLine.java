package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "TRADE_LINES",
        indexes = {
                @Index(name = "IDX_TRDL_TRADE", columnList = "TRADE_ID"),
                @Index(name = "IDX_TRDL_ITEM", columnList = "ITEM_ID")
        }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeLine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trade_line_seq")
    @SequenceGenerator(name = "trade_line_seq", sequenceName = "SEQ_TRADE_LINE", allocationSize = 1 )
    private Long id;

    //헤더 기존 엔티티 재사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="TRADE_ID", nullable = false)
    private Trade trade;

    //품목(기존 item 재사용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="ITEM_ID", nullable = false)
    private Item item;

    //수량/단가/금액
    @Column(name = "QTY", nullable = false)
    private Long qty;

    @Column(name = "UNIT_PRICE", nullable = false)
    private Long unitPrice;

    @Column(name = "SUPPLY_AMOUNT", nullable = false)
    private Long supplyAmount;

    @Column(name = "VAT_AMOUNT", nullable = false)
    private Long vatAmount;

    @Column(name = "TOTAL_AMOUNT", nullable = false)
    private Long totalAmount;

    @Column(name = "REMARK", length = 255)
    private String remark;



}
