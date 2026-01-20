package port.sm.erp.service;

/*NOT_FOUND
BAD_REQUEST
CONFLICT
FORBIDDEN
UNAUTHORIZED*/
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import port.sm.erp.common.Yn;
import port.sm.erp.dto.ItemRequest;
import port.sm.erp.dto.ItemResponse;
import port.sm.erp.entity.Item;
import port.sm.erp.entity.ItemFieldDefinition;
import port.sm.erp.entity.ItemFieldValue;
import port.sm.erp.entity.ItemType;
import port.sm.erp.repository.ItemFieldDefinitionRepository;
import port.sm.erp.repository.ItemFieldValueRepository;
import port.sm.erp.repository.ItemRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepo;
    private final ItemFieldDefinitionRepository defRepo;
    private final ItemFieldValueRepository valRepo;

    // Page형태를 반환하는 메서드 q상품명 검색, 판매중지된 상품도 포함할지 여부, 첫 페이지, 정렬기준, 정렬방향
    public Page<ItemResponse> list(String q, boolean includeStopped, int page, int size, String sortKey, String dir) {
        Pageable pageable = PageRequest.of(page, size, buildSort(sortKey, dir)); // 페이징+정렬

        // DB에서 item 목록 조회 Page<Item> 개수, 페이지수, 페이지 번호
        Page<Item> items = itemRepo.search(q, includeStopped, pageable);

        // 한방에 가져와서 매핑
        List<Long> ids = items.getContent().stream().map(Item::getId).toList(); // item id만 추출
        Map<Long, Map<String, String>> extraByItemId = loadExtraFieldsMap(ids); // extra필드를 한번에 조회

        // ✅ Map.of() 대신 Collections.emptyMap() (타입추론/제네릭 문제 방지)
        return items.map(i -> toResponse(i, extraByItemId.getOrDefault(i.getId(), Collections.emptyMap())));
        /*
            N + 1문제는 나쁘다
            아이템이 100개이면 코드 101번 실행 for(Item item : items){item.getExtraFields()}
        */
    }

    public ItemResponse get(Long id) { // 품목 하나의 상세 정보
        Item item = itemRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "품목이 없습니다. id=" + id));
        // DB에서 id에 해당하는 Item을 조회합니다 결과는 Optional<Item>형태

        Map<Long, Map<String, String>> extraById = loadExtraFieldsMap(List.of(id));
        // 해당id의 추가정보를 불러옵니다

        return toResponse(item, extraById.getOrDefault(id, Collections.emptyMap()));
        // Item + 추가정보를 합쳐서 ItemResponse로 변환
    }

    /*
     ID로 품목을 조회해서 추가 정보(extra필드)까지 붙여서 ItemResponse호 반환하는 메서드
     만약 해당 ID품목이 없으면 404에러 발생
     //자바에서 빨간줄에 원인 선언이 되지 않거나(오타) scope 밖이거나
    */

    @Transactional // 데이터 정합성을 지키기 위해 매우 중요
    public ItemResponse create(ItemRequest req) {
        validateRequired(req); // 필수값 검증

        // 품목코드 중복체크
        if (itemRepo.existsByItemCode(req.getItemCode())) {
            throw new ResponseStatusException(CONFLICT, "이미 존재 하는 품목 코드입니다:  " + req.getItemCode());
        }

        Item item = new Item();
        applyToEntity(item, req); // 엔티티 생성 및 값 매핑

        // 기본값 방어
        if (item.getItemType() == null) item.setItemType(ItemType.PRODUCT);

        itemRepo.save(item); // DB에 item 저장

        // 확장 필드 upsert
        upsertExtraFields(item, req.getExtraFields());

        // db에 저장된 extraField를 다시 조회
        Map<Long, Map<String, String>> extraById = loadExtraFieldsMap(List.of(item.getId()));

        // 최종응답생성
        return toResponse(item, extraById.getOrDefault(item.getId(), Collections.emptyMap()));
    }

    @Transactional // 전부 성공하거나 전부 취소하거나
    public ItemResponse update(Long id, ItemRequest req) {
        validateRequiredForUpdate(req); // 수정에 필요한 값 검증

        Item item = itemRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "품목이 없습니다. id=" + id));

        applyToEntity(item, req); // 요청값을 엔티티에 반영

        valRepo.deleteByItem_Id(id); // 기존 추가 필드 전부 삭제
        upsertExtraFields(item, req.getExtraFields()); // 새로운 extraFields 저장

        Map<Long, Map<String, String>> extraById = loadExtraFieldsMap(List.of(id));
        return toResponse(item, extraById.getOrDefault(id, Collections.emptyMap()));
    }

    @Transactional
    public void delete(Long id) {
        Item item = itemRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, "품목이 없습니다. id=" + id));

        // 자유필드 값 먼저 삭제 (FK때문에)
        valRepo.deleteByItem_Id(id);
        itemRepo.delete(item);
    }

    private void validateRequired(ItemRequest req) {
        if (req.getItemCode() == null || req.getItemCode().isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "품목코드는 필수입니다");
        if (req.getItemName() == null || req.getItemName().isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "품목명은 필수입니다");
        if (req.getItemType() == null)
            throw new ResponseStatusException(BAD_REQUEST, "품목구분은 필수입니다");
    }

    private void validateRequiredForUpdate(ItemRequest req) {
        if (req.getItemName() == null || req.getItemName().isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "품목명은 필수입니다");
        if (req.getItemType() == null)
            throw new ResponseStatusException(BAD_REQUEST, "품목구분은 필수입니다");
    }

    private void applyToEntity(Item item, ItemRequest req) {
        // itemCode는 create에선 필수, update에선 정책에 따라 변경 가능/불가
        if (item.getId() == null) { // create
            item.setItemCode(req.getItemCode());
        }

        item.setItemName(req.getItemName());
        item.setItemGroup(req.getItemGroup());
        item.setSpec(req.getSpec());
        item.setSpecMode(req.getSpecMode());
        item.setUnit(req.getUnit());
        item.setBarcode(req.getBarcode());
        item.setProcess(req.getProcess());

        item.setItemType(req.getItemType());

        item.setIsSetYn(Yn.toYn(req.isSet()));
        item.setInPrice(req.getInPrice());
        item.setInVatIncludedYn(Yn.toYn(req.isInVatIncluded()));

        item.setOutPrice(req.getOutPrice());
        item.setOutVatIncludedYn(Yn.toYn(req.isOutVatIncluded()));

        item.setImageUrl(req.getImageUrl());
        item.setUseYn(Yn.toYn(req.isUseYn()));
    }

    private ItemResponse toResponse(Item item, Map<String, String> extraFields) {
        return ItemResponse.builder()
                .id(item.getId())
                .itemCode(item.getItemCode())
                .itemName(item.getItemName())
                .itemGroup(item.getItemGroup())
                .spec(item.getSpec())
                .specMode(item.getSpecMode())
                .unit(item.getUnit())
                .barcode(item.getBarcode())
                .process(item.getProcess())
                .itemType(item.getItemType())
                .isSet(Yn.toBool(item.getIsSetYn()))
                .inPrice(item.getInPrice())
                .inVatIncluded(Yn.toBool(item.getInVatIncludedYn()))
                .outPrice(item.getOutPrice())
                .outVatIncluded(Yn.toBool(item.getOutVatIncludedYn()))
                .imageUrl(item.getImageUrl())
                .useYn(Yn.toBool(item.getUseYn()))
                .extraFields(extraFields)
                .build();
    }

    // ✅ 파라미터 이름(map)과 내부에서 쓰는 변수(extraFields) 불일치 수정
    private void upsertExtraFields(Item item, Map<String, String> extraFields) {
        if (extraFields == null || extraFields.isEmpty()) return;

        for (Map.Entry<String, String> e : extraFields.entrySet()) {
            String fieldKey = safeKey(e.getKey());
            String value = e.getValue();

            if (fieldKey.isBlank()) continue;

            ItemFieldDefinition def = defRepo.findByFieldKey(fieldKey)
                    .orElseGet(() -> defRepo.save(ItemFieldDefinition.builder()
                            .fieldKey(fieldKey)
                            .label(fieldKey)     // label을 따로 받으면 여기서 저장 가능
                            .fieldType("TEXT")
                            .useYn("Y")
                            .build()));

            ItemFieldValue val = ItemFieldValue.builder()
                    .item(item)
                    .fieldDef(def)
                    .fieldValue(value)
                    .build();

            valRepo.save(val);
        }
    }

    // ✅ 파라미터(ids)와 내부(itemIds) 변수명 불일치 수정
    private Map<Long, Map<String, String>> loadExtraFieldsMap(List<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) return Map.of();

        // join fetch로 def까지 한 번에 가져옴
        List<ItemFieldValue> vals = valRepo.findAllByItemIdsWithDef(itemIds);

        Map<Long, Map<String, String>> result = new HashMap<>();

        for (ItemFieldValue v : vals) {
            Long itemId = v.getItem().getId();
            String key = v.getFieldDef().getFieldKey();
            String value = v.getFieldValue();

            result.computeIfAbsent(itemId, k -> new LinkedHashMap<>()).put(key, value);
        }

        return result;
    }

    private Sort buildSort(String sortKey, String dir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Map<String, String> map = new HashMap<>();
        map.put("itemCode", "itemCode");
        map.put("itemName", "itemName");
        map.put("itemGroup", "itemGroup");
        map.put("spec", "spec");
        map.put("barcode", "barcode");
        map.put("inPrice", "inPrice");
        map.put("outPrice", "outPrice");
        map.put("itemType", "itemType");
        map.put("image", "imageUrl");
        map.put("imageUrl", "imageUrl");
        map.put("useYn", "useYn");

        String mapped = (sortKey == null) ? null : map.get(sortKey);

        if (mapped == null || mapped.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "id"); // ✅ 기본 최신순
        }

        return Sort.by(direction, mapped).and(Sort.by(Sort.Direction.DESC, "id"));
    }


    private String safeKey(String s) {
        if (s == null) return "";
        return s.trim();
    }
}
