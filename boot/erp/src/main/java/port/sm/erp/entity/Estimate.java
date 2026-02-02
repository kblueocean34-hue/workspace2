package port.sm.erp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ESTIMATES")
public class Estimate {

    @Id //견적서 번호가 아니라 DB용 고유id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estimate_seq")
    @SequenceGenerator(
            name = "estimate_seq", sequenceName = "SEQ_ESTIMATE", allocationSize = 1
    )
    private Long id;

    @Column(nullable = false) //받드시 값이 있어야 함
    private String estimateNo; //사람이 보는 견적서 번호
    private LocalDate estimateDate;

    @Column(nullable = false)
    private String customerName; //견적 대상 고객이름
    private String remark; //견적서 하단에 메모 같은 용도

    //견적서에 견적서 항목 견적서 1건(Estimate) 견적 항목 여러 개(EstimateLine)
    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimateLine> lines = new ArrayList<>();

}