package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
   name = "INV_STOCK_MOVEMENT",
        indexes = {
                @Index(name = "IDX_STK_MV_ITEM", columnList = "ITEM_ID"),
                @Index(name = "IDX_STK_MV_AT", columnList = "MOVED_AT"),
                @Index(name = "IDX_STK_MV_REF", columnList = "REF_TYPE, REF_ID")
        }
)
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INV_STK_MV_SEQ_GEN")
    @SequenceGenerator(
            name = "INV_STK_MV_SEQ_GEN",
            sequenceName = "SEQ_INV_STOCK_MOVEMENT",
            allocationSize = 1
    )
    @Column(name = "ID", columnDefinition = "NUMBER(19)")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id", nullable = false)
    private Item item;

    //
    @Enumerated(EnumType.STRING)
    @Column(name = "MOVE_TYPE", nullable = false, length= 20)
    private StockTxnType moveType;

    @Column(name = "QTY", nullable = false)
    private Long qty;

    @Column(name = "BEFORE_QTY", nullable = false, columnDefinition = "NUMBER(19)")
    private Long beforeQty = 0L;

    @Column(name = "AFTER_QTY", nullable = false, columnDefinition = "NUMBER(19)")
    private Long afterQty = 0L;

    @Column(name = "MOVED_AT", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime movedAt;


    @Column(name = "REF_TYPE", length = 30)
    private String refType;

    @Column(name = "REF_ID", columnDefinition = "NUMBER(19)")
    private Long refId;

    //작업자 기존 맴버 fk
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "MEMBER_ID")
    private Member user;
//
    @Column(name = "REMARK", length = 500)
    private String remark;
}
