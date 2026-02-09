package port.sm.erp.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "TRADE_INPUT",
        indexes = {
                @Index(name = "IDX_SI2_TRADE", columnList = "TRADE_ID"),
                @Index(name = "IDX_SI2_DATE", columnList = "TRADE_DATE"),
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TradeInput {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trade_input_seq")
    @SequenceGenerator(
            name = "trade_input_seq",
            sequenceName = "SEQ_TRADE_INPUT",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    //기존 trade와 1대 1연결
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRADE_ID", nullable = false, unique = true)
    private Trade trade;

    @Column(name ="TRADE_DATE", nullable = false)
    private LocalDate tradeDate;

    @Column(name ="DELIVERY_DATE")
    private LocalDate deliveryDate;

    @Column(name ="DELIVERY_ADDRESS", length = 300)
    private String deliveryAddress;

    @Column(name ="CONTACT_TEL", length = 50)
    private String contactTel;

    @Column(name ="REMARK", length = 500)
    private String remark;

}
