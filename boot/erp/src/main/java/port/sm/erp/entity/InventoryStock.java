package port.sm.erp.entity;

import javax.persistence.*;
import lombok.*;

//등록일 / 수정일 자동 기록용 어노테이션.
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

//감사(Auditing) 기능 활성화용 리스너.
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.validation.constraints.Min; //최소값 검증(음수 방지)

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
name = "INV_STOCK",
        uniqueConstraints = @UniqueConstraint( name = "UK_INV_STOCK_ITEM", columnNames = "ITEM_ID"),
        indexes = @Index(name = "IDX_INV_STOCK_ITEM", columnList = "ITEM_ID")
) //ITEM_ID 중복금지 조회/조인 할때 속도가 빨라직 위해서
@EntityListeners(AuditingEntityListener.class) //생성/수정 시간 자동 기록 기능 활성화.
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryStock {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INV_STOCK")
    @SequenceGenerator(name = "SEQ_INV_STOCK", sequenceName = "SEQ_INV_STOCK", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    //버전 (동시성 제어)
    @Version //동시에 수정 시 재고 꼬임 방지.
    @Column(name = "VERSION" , nullable = false)
    private Long version;

    //아이템 연관관계
    @ManyToOne(fetch = FetchType.LAZY) //이 재고는 하나의 Item을 참조한다는 뜻. (재고 N개 → 아이템 1개)
    //LAZY는 “Item 정보는 필요할 때(DB에서) 가져온다”는 뜻 → 성능을 위해 기본적으로 지연로딩.
    //반대는 즉시 로딩(FetchType.EAGER)
    @JoinColumn(name = "ITEM_ID", nullable = false) //외래키 컬럼 이름이 ITEM_ID라는 뜻.
    private Item item;

    //수량 컬럼
    @Min(0)
    @Column(name="ON_HAND_QTY", nullable = false) //DB 컬럼 ON_HAND_QTY null 불가(반드시 값 있어야 함)
    private Long onHandQty = 0L; //현재 보유 수량(물리적으로 창고에 있는 수량)

    @Min(0)
    @Column(name ="RESERVED_QTY", nullable = false)
    private Long reservedQty = 0L;

    @Min(0)
    @Column(name ="AVAILABLE_QTY", nullable = false)
    private Long availableQty = 0L;

    @Min(0)
    @Column(name ="SAFETY_QTY", nullable = false)
    private Long safetyQty = 0L;
//이동일자
    @Column(name ="LAST_MOVED_AT")
    private java.time.LocalDate lastMovedAt;

    //검사필드 최초 등록 시간 자동 저장 수정 불가.
    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;
    
    //마지막 수정 시간 자동갱신
    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updateAt;
    
    //자동계산 메서드
    @PrePersist
    @PreUpdate
    private void syncAvailableQty(){ //가용 수량 계산 메서드 시작
        long onHand = (onHandQty == null) ? 0L : onHandQty; //보유수량 null 방지.
        long reserved = (reservedQty == null) ? 0L : reservedQty;//예약수량 null 방지.
        long v = onHand - reserved; //가용 수량 계산.
        this.availableQty = Math.max(0L, v); //음수 방지 최소 0으로 보정.
    }


}
