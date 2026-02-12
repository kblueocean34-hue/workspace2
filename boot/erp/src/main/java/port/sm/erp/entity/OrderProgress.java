package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "ORDER_PROGRESS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderProgress {

    /** =========================
     * PK
     * ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_progress_seq")
    @SequenceGenerator(
            name = "order_progress_seq",
            sequenceName = "SEQ_ORDER_PROGRESS",
            allocationSize = 1
    )
    @Column(name = "ORDER_PROGRESS_ID")
    private Long id;

    /** =========================
     * 오더 관리 정보
     * ========================= */
    @Column(name = "ORDER_NO", nullable = false, length = 50)
    private String orderNo;          // 오더관리번호

    @Column(name = "ORDER_NAME", nullable = false, length = 200)
    private String orderName;        // 오더관리명

    @Column(name = "PROGRESS_TEXT", length = 500)
    private String progressText;     // 진행단계 텍스트 (제작중, 출고완료 등)

    /** =========================
     * 작성자 (Member FK)
     * ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")   // MEMBERS.MEMBER_ID FK
    private Member member;

    /** =========================
     * 상태값 (선택)
     * ========================= */
    @Column(name = "STATUS", length = 30)
    private String status; // ACTIVE, DELETED 등

    /** =========================
     * 생성 / 수정 일시
     * ========================= */
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT", insertable = false, updatable = false)
    private Date updatedAt;
}
