package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "PL_STATEMENT", indexes = {
                @Index(name ="IDX_PL_PERIOD", columnList = "PERIOD_FROM, PERIOD_TO")
}
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PlStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pl_stmt_seq")
    @SequenceGenerator(name = "pl_stmt_seq", sequenceName = "SEQ_PL_STATEMENT", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PERIOD_FROM", nullable = false)
    private LocalDate periodFrom;

    @Column(name = "PERIOD_TO", nullable = false)
    private LocalDate periodTo;

    @Column(name = "GENERATED_AT", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime generatedAt;

    //작업자 기존 멤버에서 재사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="USER_ID", referencedColumnName = "MEMBER_ID")
    private Member user;

    @OneToMany(mappedBy = "statement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlStatementLine> lines = new ArrayList<>();


}
