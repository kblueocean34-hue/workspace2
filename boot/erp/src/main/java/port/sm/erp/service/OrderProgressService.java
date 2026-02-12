package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.entity.OrderProgress;
import port.sm.erp.repository.OrderProgressRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderProgressService {

    private final OrderProgressRepository orderProgressRepository;

    /**
     * 등록
     */
    public OrderProgress create(OrderProgress orderProgress) {

        // ✅ 오더번호가 없으면 자동 생성
        if (orderProgress.getOrderNo() == null || orderProgress.getOrderNo().isBlank()) {

            Long seq = orderProgressRepository.getNextOrderNoSequence();

            String year = String.valueOf(java.time.Year.now().getValue());

            String orderNo = String.format("ord-%s-%04d", year, seq);

            orderProgress.setOrderNo(orderNo);
        }

        orderProgress.setStatus("ACTIVE");
        return orderProgressRepository.save(orderProgress);
    }

    /**
     * ID로 단건 조회
     */
    @Transactional(readOnly = true)
    public OrderProgress getById(Long id) {
        return orderProgressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 진행정보가 없습니다. id=" + id));
    }

    /**
     * 오더관리번호로 단건 조회
     */
    @Transactional(readOnly = true)
    public OrderProgress getByOrderNo(String orderNo) {
        return orderProgressRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 오더번호가 없습니다. orderNo=" + orderNo));
    }

    /**
     * 오더명 LIKE 검색 (List)
     */
    @Transactional(readOnly = true)
    public List<OrderProgress> searchByOrderName(String keyword) {
        return orderProgressRepository.findByOrderNameContaining(keyword);
    }

    /**
     * 오더명 LIKE 검색 (페이징)
     */
    @Transactional(readOnly = true)
    public Page<OrderProgress> searchByOrderName(String keyword, Pageable pageable) {
        return orderProgressRepository.findByOrderNameContaining(keyword, pageable);
    }

    /**
     * 상태값으로 조회
     */
    @Transactional(readOnly = true)
    public List<OrderProgress> getByStatus(String status) {
        return orderProgressRepository.findByStatus(status);
    }

    /**
     * 작성자 기준 조회
     */
    @Transactional(readOnly = true)
    public List<OrderProgress> getByMemberId(Long memberId) {
        return orderProgressRepository.findByMember_Id(memberId);
    }

    /**
     * 수정 (Dirty Checking)
     */
    public void update(Long id, OrderProgress request) {
        OrderProgress entity = getById(id);

        entity.setOrderNo(request.getOrderNo());
        entity.setOrderName(request.getOrderName());
        entity.setProgressText(request.getProgressText());
        entity.setStatus(request.getStatus());
        entity.setMember(request.getMember());
    }

    /**
     * 삭제 (Soft Delete)
     */
    public void delete(Long id) {
        OrderProgress entity = getById(id);
        entity.setStatus("DELETED");
    }
}
