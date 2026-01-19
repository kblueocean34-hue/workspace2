package port.sm.erp.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.*;
import port.sm.erp.entity.ItemType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {
	
	private String itemCode;
	private String itemName;
	private String itemGroup;
	private String spec;
	private String specMode;
	private String unit;
	private String barcode;
	private String process;
	
	private ItemType itemType;
	private boolean set;
	
	private BigDecimal inPrice;
	private boolean inVatIncluded;
	
	private BigDecimal outPrice;
	private boolean outVatIncluded;
	
	private String imageUrl;
	private boolean useYn;
	
	private Map<String, String> extraFields;
	
	

}
