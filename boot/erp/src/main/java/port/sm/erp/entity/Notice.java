package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTICES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true) //기존 객체에서 수정 가능한 Builder를 만들 수 있음
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notice_seq")
    @SequenceGenerator(
            name = "notice_seq", sequenceName = "NOTICE_SEQ", allocationSize = 1
    )
    @Column(name = "NOTICE_ID")
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    //Large OBject  @Lob
    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "VIEW_COUNT")
    private Long viewCount = 0L;

    @Column(name = "IS_PINNED")
    private Boolean isPinned = false;

    //fk
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT",insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseViewCount(){
        this.viewCount++;
    }

}
