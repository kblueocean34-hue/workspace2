package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.JournalLineResponse;
import port.sm.erp.dto.JournalResponse;
import port.sm.erp.dto.JournalSearchRequest;
import port.sm.erp.entity.Journal;
import port.sm.erp.repository.JournalRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

//“전표를 조회해서 화면에 내려주는 서비스(Service)” 단계
@Service /*
비즈니스 로직 담당 클래스
Controller: 요청 받기
Repository: DB 접근
Service: 중간에서 실제 처리
전표 목록 주세요” → 여기서 처리
*/
@RequiredArgsConstructor
//👉 final 필드만 생성자로 자동 주입
@Transactional(readOnly = true)
//👉 이 서비스는 조회 전용 DB 수정 ❌ 성능 ⬆ 실수로 save/delete 막아줌
public class JournalService {

    private final JournalRepository journalRepository;

    public Page<JournalResponse> list(
            JournalSearchRequest req,
            Pageable pageable
    ) {
        Page<Journal> page = journalRepository.findAll(pageable);
        return page.map(this::toResponse);
    }

    private JournalResponse toResponse(Journal j) {
        List<JournalLineResponse> lines = j.getLines().stream()
                .map(l -> new JournalLineResponse(
                        l.getAccountCode(),
                        l.getAccountName(),
                        l.getDcType(),
                        l.getAmount().longValue()
                )).toList();
        return new JournalResponse(
                j.getId(), j.getJournalDate(), lines
        );
    }
}
