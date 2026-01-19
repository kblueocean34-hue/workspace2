package port.sm.erp.entity;

import javax.persistence.*;
import lombok.*;
import java.math.*;

@Entity
@Table(
name = "INV_ITEM",
uniqueConstraints = {
@UniqueConstraint(name = "UK_INV_ITEM_CODE", columnNames = "ITEM_CODE")
},indexes = {
@Index(name = "IDX_INV_ITEM_NAME", columnList="ITEM_NAME"),
@Index(name = "IDX_INV_ITEM_GROUP", columnList="ITEM_GROUP"),
@Index(name = "IDX_INV_ITEM_BARCODE", columnList="BARCODE"),
@Index(name = "IDX_INV_ITEM_USE_YN", columnList="USE_YN")
}
)
@Getter @Setter
@NoArgsConstructor //밑에 항목을 한줄이라도 안적으면 빨간줄이 뜬다
@AllArgsConstructor
@Builder
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INV_ITEM")
	@SequenceGenerator(
	name = "SEQ_INV_ITEM",
	sequenceName = "SEQ_INV_ITEM",
	allocationSize = 1
	)
	@Column(name="ID")
	private Long id;
	
	@Column(name="ITEM_CODE", nullable = false, length=50)
	private String itemCode;
	
	@Column(name="ITEM_NAME", nullable = false, length=200)
	private String itemName;
	
	@Column(name="ITEM_GROUP", length=100)
	private String itemGroup;
	
	@Column(name="SPEC", length=200)
	private String spec;
	
	@Column(name="SPEC_MODE", length=30)
	private String specMode;
	
	@Column(name="UNIT", length=30)
	private String unit;
	
	@Column(name="BARCODE", length=100)
	private String barcode;
	
	@Column(name="PROCESS", length=100)
	private String process;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ITEM_TYPE", nullable=false, length=30)
	private ItemType itemType;
	
	//오라클에서는 boolean컬럼이 없어서 보통 CHAR(1) Y/N로 저장합니다
	//오라클 Y/N
	@Column(name="IS_SET_YN", nullable=false, length=1)
	@Builder.Default
	private String isSetYn = "N";
	
	@Column(name="IN_PRICE", precision=18, scale=2)
	private BigDecimal inPrice;
	
	@Column(name="IN_VAT_INCLUDED_YN", nullable=false, length=1)
	@Builder.Default
	private String inVatIncludedYn = "N";
	
	@Column(name="IMAGE_URL", length=500)
	private String imageUrl;
	
	//사용여부
	@Column(name="USE_YN", nullable=false, length=1)
	@Builder.Default
	private String useYn = "Y";
	
	@Column(name="OUT_PRICE", precision=18, scale=2)
	private BigDecimal outPrice;

	@Column(name="OUT_VAT_INCLUDED_YN", nullable=false, length=1)
	@Builder.Default
	private String outVatIncludedYn = "N";

	

}
