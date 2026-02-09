package port.sm.erp.entity;

import javax.persistence.*;

@Entity
@Table(
name = "INV_STOCK",
        uniqueConstraints = @UniqueConstraint( name = "UK_INV_STOCK_ITEM", columnNames = "ITEM_ID"),
        indexes = @Index(name = "IDX_INV_STOCK_ITEM", columnList = "ITEM_ID")
) //ITEM_ID 중복금지 조회/조인 할때 속도가 빨라직 위해서
public class InventoryStock {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INV_STOCK")
    @SequenceGenerator(name = "SEQ_INV_STOCK", sequenceName = "SEQ_INV_STOCK", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //이 재고는 하나의 Item을 참조한다는 뜻. (재고 N개 → 아이템 1개)
    //LAZY는 “Item 정보는 필요할 때(DB에서) 가져온다”는 뜻 → 성능을 위해 기본적으로 지연로딩.
    //반대는 즉시 로딩(FetchType.EAGER)
    @JoinColumn(name = "ITEM_ID", nullable = false) //외래키 컬럼 이름이 ITEM_ID라는 뜻.
    private Item item;

    //수량 컬럼
    @Column(name="ON_HAND_QTY", nullable = false) //DB 컬럼 ON_HAND_QTY null 불가(반드시 값 있어야 함)
    private Long onHandQty; //현재 보유 수량(물리적으로 창고에 있는 수량)

    @Column(name ="RESERVED_QTY", nullable = false)
    private Long reservedQty;

    @Column(name ="AVAILABLE_QTY", nullable = false)
    private Long availableQty;

    @Column(name ="SAFETY_QTY", nullable = false)
    private Long safetyQty;

    @Column(name ="LAST_MOVED_AT")
    private java.time.LocalDate lastMovedAt;

}
