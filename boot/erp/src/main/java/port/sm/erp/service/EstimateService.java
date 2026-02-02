package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.EstimateLineRequest;
import port.sm.erp.dto.EstimateLineResponse;
import port.sm.erp.dto.EstimateRequest;
import port.sm.erp.dto.EstimateResponse;
import port.sm.erp.entity.Estimate;
import port.sm.erp.entity.EstimateLine;
import port.sm.erp.repository.EstimateRepository;

import java.math.BigDecimal;
import java.util.List;

@Service //비즈니스 로직 담당 클래스 컨트롤러 ↔ 레포지토리 사이의 중간 계층
@RequiredArgsConstructor //final 필드만 모아서 생성자 자동 생성
@Transactional //이 클래스의 모든 public 메서드는 트랜잭션 안에서 실행 중간에 예외 터지면 → 자동 롤백
public class EstimateService {
//DB접근 전담 Estimate 엔티티를 저장/조회/삭제

    //👉 생성자 주입됨 (@RequiredArgsConstructor 덕분)
    private final EstimateRepository estimateRepository;

    @Transactional(readOnly = true) //👉 조회 전용 트랜잭션 성능 최적화 + 수정 방지
    public List<EstimateResponse> list() {
        //👉 견적서 전체 목록 반환 👉 견적일자 최신순 정렬
        return estimateRepository.findAll(Sort.by(Sort.Direction.DESC, "estimateDate"))
                .stream()//리스트를 스트림으로 변환
                .map(this::toResponse)//👉 엔티티 → 화면용 DTO로 변환
                .toList(); //👉 다시 List로 변환해서 반환
    }

    private EstimateResponse toResponse(Estimate e) { //👉 견적서 하나를 DTO로 바꾸는 함수
        List<EstimateLineResponse> lines =
                //👉 견적서 안의 라인 DTO 리스트 만들기
                e.getLines().stream()//👉 견적서에 속한 라인 목록 가져오기
                        .map(l -> new EstimateLineResponse(//👉 라인 하나를 DTO로 변환
l.getId(),
l.getItemName(),
l.getQty(),
l.getPrice(),
l.getAmount(),
l.getRemark() //👉 엔티티 값 → DTO 필드로 복사
))
.toList(); //👉 라인 DTO 리스트 완성

return new EstimateResponse(//👉 견적서 정보 + 라인 리스트 전달
        e.getId(),
        e.getEstimateNo(),
        e.getEstimateDate(),
        e.getCustomerName(),
        e.getRemark(),
        lines
);
}

    //단건 id로 db조회
    @Transactional(readOnly = true)
    public EstimateResponse get(Long id){
        Estimate e = estimateRepository.findById(id).orElseThrow(
                () -> new RuntimeException("견적서 없음"));
        return toResponse(e);
    }

    //등록
    public void create(EstimateRequest req){
        Estimate e = new Estimate(); //👉 빈 견적서 객체 생성
        apply(e, req); //👉 화면에서 온 데이터 채우기
        estimateRepository.save(e);//👉 DB 저장 (라인도 같이 저장됨)
    }

    //수정
    public void update(Long id, EstimateRequest req) { //👉 기존 견적서 수정
        Estimate e = estimateRepository.findById(id) //👉 수정할 견적서 조회
                .orElseThrow(() -> new RuntimeException("견적서 없음"));
    e.getLines().clear();
    apply(e, req);
    }

    //삭제
    public void delete(Long id) {
        estimateRepository.deleteById(id);
    }

    //📌 공통 적용 로직 👉 등록/수정 공통 처리 함수
    private void apply(Estimate e, EstimateRequest req){
        e.setEstimateNo(req.getEstimateNo()); //👉 견적번호 세팅
        e.setEstimateDate(req.getEstimateDate()); // 👉 견적번호 세팅
        e.setCustomerName(req.getCustomerName()); // 👉 견적일자 세팅
        e.setRemark(req.getRemark()); //👉 비고 세팅

        for (EstimateLineRequest l : req.getLines()) { //견적라인 하나씩 처리
            EstimateLine line = new EstimateLine(); //👉 라인 엔티티 생성
            line.setItemName(l.getItemName()); //👉 품목명
            line.setQty(l.getQty());//👉 수량
            line.setPrice(l.getPrice()); // 👉 단가
            line.setAmount(
                    l.getPrice().multiply(BigDecimal.valueOf(l.getQty()))
            );
            //👉 금액 = 단가 × 수량

            line.setEstimate(e); //어느 견적서 소속인지 연결
            e.getLines().add(line); //견적서에 라인 추가
        }
    }



}
