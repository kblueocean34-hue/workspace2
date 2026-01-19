package port.sm.erp.entity;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(
    name="INV_ITEM_FIELD_VAL",
    uniqueConstraints = {
        @UniqueConstraint(
            name="UK_INV_ITEM_FIELD_VAL",
            columnNames={"ITEM_ID","FIELD_DEF_ID"}
        )
    },
    indexes = {
        @Index(name="IDX_INV_ITEM_FIELD_VAL_ITEM", columnList="ITEM_ID")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ItemFieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INV_ITEM_FIELD_VAL")
    @SequenceGenerator(
        name = "SEQ_INV_ITEM_FIELD_VAL",
        sequenceName = "SEQ_INV_ITEM_FIELD_VAL",
        allocationSize = 1
    )
    @Column(name="ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name="ITEM_ID",
        nullable=false,
        foreignKey=@ForeignKey(name="FK_INV_ITEM_FIELD_VAL_ITEM")
    )
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name="FIELD_DEF_ID",
        nullable=false,
        foreignKey=@ForeignKey(name="FK_INV_ITEM_FIELD_VAL_DEF")
    )
    private ItemFieldDefinition fieldDef;

    // 값은 일단 문자열로 저장 (필요 시 fieldType 따라 변환)
    @Column(name="FIELD_VALUE", length=1000)
    private String fieldValue;
}
