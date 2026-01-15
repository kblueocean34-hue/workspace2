package port.sm.erp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import port.sm.erp.entity.CalendarEvent;
import port.sm.erp.repository.CalendarEventRepository;

@Service
@RequiredArgsConstructor
@Transactional // ✅ 기본은 쓰기 트랜잭션
public class CalendarEventService {

    private final CalendarEventRepository repo;

    /**
     * ✅ 월/기간 일정 조회 (내 일정 + 공유받은 일정)
     */
    @Transactional(readOnly = true)
    public List<CalendarEvent> list(Long userId, LocalDate from, LocalDate to) {
        List<CalendarEvent> mine = repo.findByUserIdAndDateBetweenOrderByDateAsc(userId, from, to);

        // ✅ 공유 일정 (@Query 메서드: String 파라미터)
        List<CalendarEvent> shared = repo.findSharedWithUser(String.valueOf(userId), from, to);

        // ✅ 중복 제거(id 기준) - null 안전 비교
        List<CalendarEvent> merged = new ArrayList<>(mine);
        for (CalendarEvent ev : shared) {
            boolean exists = merged.stream().anyMatch(m -> Objects.equals(m.getId(), ev.getId()));
            if (!exists) merged.add(ev);
        }

        // ✅ 정렬: date -> startTime(HH:mm) (없으면 뒤로)
        merged.sort((a, b) -> {
            int c = a.getDate().compareTo(b.getDate());
            if (c != 0) return c;
            String as = (a.getStartTime() == null || a.getStartTime().isBlank()) ? "99:99" : a.getStartTime();
            String bs = (b.getStartTime() == null || b.getStartTime().isBlank()) ? "99:99" : b.getStartTime();
            return as.compareTo(bs);
        });

        return merged;
    }

    /**
     * ✅ 상세 조회 (내 일정 or 공유 일정이면 OK)
     */
    @Transactional(readOnly = true)
    public CalendarEvent get(Long userId, Long id) {
        CalendarEvent ev = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다. id=" + id));

        if (!canRead(userId, ev)) {
            throw new SecurityException("권한이 없습니다.");
        }
        return ev;
    }

    /**
     * ✅ 일정 생성 (내 일정)
     */
    @Transactional
    public CalendarEvent create(Long userId, CalendarEvent body) {
        body.setId(null);
        body.setUserId(userId);

        validateRequired(body);

        if (body.getAttendees() == null) body.setAttendees(new ArrayList<>());
        if (body.getSharers() == null) body.setSharers(new ArrayList<>());

        return repo.save(body);
    }

    /**
     * ✅ 일정 수정 (내 일정만)
     */
    @Transactional
    public CalendarEvent update(Long userId, Long id, CalendarEvent body) {
        CalendarEvent ev = repo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new SecurityException("수정 권한이 없거나 일정이 존재하지 않습니다. id=" + id));

        ev.setDate(body.getDate());
        ev.setTitle(body.getTitle());
        ev.setMemo(body.getMemo());

        ev.setStartTime(body.getStartTime());
        ev.setEndTime(body.getEndTime());

        ev.setCategory(body.getCategory());
        ev.setCalendar(body.getCalendar());
        ev.setLocation(body.getLocation());
        ev.setLabel(body.getLabel());

        // ✅ 컬렉션은 교체하지 말고 내용만 덮기
        ev.getAttendees().clear();
        if (body.getAttendees() != null) ev.getAttendees().addAll(body.getAttendees());

        ev.getSharers().clear();
        if (body.getSharers() != null) ev.getSharers().addAll(body.getSharers());

        validateRequired(ev);
        return ev; // 영속 상태라 커밋 시 자동 update
    }

    /**
     * ✅ 일정 삭제 (내 일정만)
     */
    @Transactional
    public void delete(Long userId, Long id) {
        CalendarEvent ev = repo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new SecurityException("삭제 권한이 없거나 일정이 존재하지 않습니다. id=" + id));
        repo.delete(ev); // ✅ 누락되어 있던 실제 삭제
    }

    // ---------------------------
    // 내부 유틸
    // ---------------------------

    private boolean canRead(Long userId, CalendarEvent ev) {
        if (ev.getUserId() != null && ev.getUserId().equals(userId)) return true;
        return ev.getSharers() != null && ev.getSharers().contains(String.valueOf(userId));
    }

    private void validateRequired(CalendarEvent ev) {
        if (ev.getDate() == null) throw new IllegalArgumentException("date는 필수입니다.");
        if (ev.getTitle() == null || ev.getTitle().trim().isEmpty()) throw new IllegalArgumentException("title은 필수입니다.");
    }
}
