package port.sm.erp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "APPROVAL_DOC",
        indexes ={
@Index(name = "IDX_APR_DOC_DRAFT_DATE", columnList = "DRAFT_DATE"),
@Index(name = "IDX_APR_DOC_STATUS", columnList = "STATUS"),
@Index(name = "IDX_APR_DOC_DRAFTER_ID", columnList = "DRAFTER_ID"),
@Index(name = "IDX_APR_DOC_APPROVER_ID", columnList = "APPROVER_ID"),
        }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "approval_doc_seq")
    @SequenceGenerator(
            name = "approval_doc_seq",
            sequenceName = "SEQ_APPROVAL_DOC",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    // 기안일자
    @Column(name = "DRAFT_DATE", nullable = false)
    private LocalDate draftDate;

    // 제목
    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    // ✅ 기안자 FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DRAFTER_ID", nullable = false)
    private Member drafter;

    // ✅ 결재자 FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "APPROVER_ID", nullable = false)
    private Member approver;

    // 진행상태
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private ApprovalStatus status;

    // 내용 (Oracle CLOB)
    @Lob
    @Column(name = "CONTENT")
    private String content;

    // 생성/수정일
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = (this.createdAt == null) ? now : this.createdAt;
        this.updatedAt = now;
        if (this.status == null) this.status = ApprovalStatus.DRAFT;
        if (this.draftDate == null) this.draftDate = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
