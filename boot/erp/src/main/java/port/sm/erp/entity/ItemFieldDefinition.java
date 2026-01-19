package port.sm.erp.entity;



import javax.persistence.*;

import lombok.*;

@Entity
@Table(
name ="INV_ITEM_FIELD_DEF",
			uniqueConstraints = {
					@UniqueConstraint(name="UK_INV_ITEM_FIELD_KEY", columnNames="FIELD_KEY")	
			}
		)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemFieldDefinition {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INV_ITEM_FIELD_DEF")
	@SequenceGenerator(
	name = "SEQ_INV_ITEM_FIELD_DEF",
	sequenceName = "SEQ_INV_ITEM_FIELD_DEF",
	allocationSize = 1
	)
	@Column(name="ID")
	private Long id;
	
	@Column(name="FIELD_KEY", nullable=false, length=80)
	private String fieldKey;
	
    // TEXT / NUMBER / DATE 등
    @Column(name="FIELD_TYPE", nullable=false, length=20)
    @Builder.Default
    private String fieldType = "TEXT";

    @Column(name="USE_YN", nullable=false, length=1)
    @Builder.Default
    private String useYn = "Y";
	
}
