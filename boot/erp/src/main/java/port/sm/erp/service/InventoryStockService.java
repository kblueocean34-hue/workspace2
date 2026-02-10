package port.sm.erp.service; // ✅ 서비스(비즈니스 로직) 계층 패키지

import lombok.RequiredArgsConstructor; // ✅ final 필드 생성자 자동 생성(Lombok)

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;// ✅ Spring 서비스 빈 등록
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.InventoryStockRequest;
import port.sm.erp.dto.InventoryStockResponse;
import port.sm.erp.entity.InventoryStock;
import port.sm.erp.entity.Item;
import port.sm.erp.repository.InventoryStockRepository;
import port.sm.erp.repository.ItemRepository;

@Service // ✅ 이 클래스는 서비스(비즈니스 로직) 빈이다
@RequiredArgsConstructor //final 필드(아래 2개)를 자동 생성자로 만들어서 DI
@Transactional //클래스 전체 기본 트랜잭션 적용
public class InventoryStockService {

    private final InventoryStockRepository stockRepository; //의존성 주입 재고 테이블 CRUD/검색 담당.
    private final ItemRepository itemRepository; // ✅ 품목 조회/검증을 위한 레포지토리 주입

    @Transactional(readOnly = true) //읽기 전용 트랜잭션(성능/안전).
    public Page<InventoryStockResponse> list(Pageable pageable, String q) {
        // ✅ 페이징 + 검색어로 목록 반환

        Page<InventoryStock> page = (q == null || q.trim().isEmpty())// ✅ q가 null이거나 공백이면
                ? stockRepository.findAll(pageable)// ✅ 전체 목록 페이징 조회
                : stockRepository.search(q.trim(), pageable);// ✅ 검색어로 품목코드/품목명 LIKE 검색

        return page.map(InventoryStockService::toResponse);// ✅ 엔티티 Page -> DTO Page 변환해서 반환
    }

    //단건
    @Transactional(readOnly = true)// ✅ 읽기 전용 트랜잭션
    public InventoryStockResponse get(Long id) { // ✅ id로 재고 단건 조회
        InventoryStock s = stockRepository.findById(id).orElseThrow(// ✅ 재고 엔티티 조회
                () -> new IllegalArgumentException("재고 없음 :" +id)  // ✅ 없으면 예외
        );

        return toResponse(s); // ✅ 엔티티 -> 응답 DTO로 변환 후 반환
    }

    //등록 변경
    public InventoryStockResponse create(InventoryStockRequest req) {

        // ✅ 0) itemId 필수 체크 (프론트에서 안 보내면 여기서 먼저 터뜨리기)
        if (req.getItemId() == null) {
            throw new IllegalArgumentException("itemId는 필수입니다.");
        }

        // ✅ 1) 품목 존재 확인
        Item item = itemRepository.findById(req.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("품목 없음: " + req.getItemId()));

        // ✅ 2) itemId는 inv_stock에서 UNIQUE라서 중복 등록 방지
        if (stockRepository.existsByItem_Id(req.getItemId())) {
            throw new IllegalStateException("이미 재고가 등록된 품목입니다. itemId=" + req.getItemId());
        }

        // ✅ 3) 저장
        InventoryStock s = InventoryStock.builder()
                .item(item)
                .onHandQty(nvl(req.getOnHandQty()))
                .reservedQty(nvl(req.getReservedQty()))
                .safetyQty(nvl(req.getSafetyQty()))
                .build();

        InventoryStock saved = stockRepository.save(s);
        return toResponse(saved);
    }


    //수정
    public InventoryStockResponse update(Long id, InventoryStockRequest req){// ✅ 재고 수정(부분 업데이트)
        InventoryStock s  = stockRepository.findById(id)// ✅ 수정 대상 조회
                .orElseThrow(() -> new IllegalArgumentException("재고 없음 :" + id));

        if(req.getOnHandQty() != null)// ✅ 요청에 현재고가 들어오면
            s.setOnHandQty(nvl(req.getOnHandQty()));// ✅ 현재고 업데이트(널 방지)

        if(req.getReservedQty() != null)// ✅ 요청에 예약수량이 들어오면
            s.setReservedQty(nvl(req.getReservedQty()));// ✅ 예약수량 업데이트

        if(req.getSafetyQty() != null)// ✅ 요청에 안전재고가 들어오면
            s.setSafetyQty(nvl(req.getSafetyQty()));// ✅ 안전재고 업데이트

        return toResponse(s);
    }

    //삭제
    public void delete(Long id) {
        if (!stockRepository.existsById(id)){ //삭제 대상 존재 여부 확인
            throw new IllegalArgumentException("재고 없음 : " +id);
        }
        stockRepository.deleteById(id);//존재하면 삭제 실행
    }

    //엔티티 리스판스 변환
    public static InventoryStockResponse toResponse(InventoryStock s){
        return InventoryStockResponse.builder()
                .id(s.getId())
                .itemId(s.getItem().getId())
                .itemCode(s.getItem().getItemCode())
                .itemName(s.getItem().getItemName())
                .onHandQty(s.getOnHandQty())
                .reservedQty(s.getReservedQty())
                .availableQty(s.getAvailableQty())
                .safetyQty(s.getSafetyQty())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdateAt())
                .build();
    }

    private static long nvl(Long v){ //Long이 null이면 0으로 바꿔주는 헬퍼
        return v == null ? 0L : v; //null -> 0, 아니면 그대로 반환
    }

}
