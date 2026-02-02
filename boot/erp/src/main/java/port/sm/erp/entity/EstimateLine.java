package port.sm.erp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ESTIMATE_LINES")
public class EstimateLine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estimate_line_seq")
    @SequenceGenerator(
            name ="estimate_line_seq", sequenceName =  "SEQ_ESTIMATE_LINE", allocationSize = 1
    )
    private Long id;

    private String itemName;
    private Integer qty;
    private BigDecimal price;
    private BigDecimal amount;
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTIMATE_ID")
    private Estimate estimate;
}
