package port.sm.erp.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import port.sm.erp.entity.ItemType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {

	private Long id;
	
private String itemCode, itemName, itemGroup, spec, specMode, unit, barcode, process, imageUrl;
private ItemType itemType;
private BigDecimal inPrice, outPrice;
private boolean inVatIncluded, outVatIncluded, useYn, isSet;
private Map<String, String> extraFields;



}
