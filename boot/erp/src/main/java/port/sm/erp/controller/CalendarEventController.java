package port.sm.erp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import port.sm.erp.entity.CalendarEvent;
import port.sm.erp.repository.MemberRepository;
import port.sm.erp.service.CalendarEventService;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class CalendarEventController {

	private static final Logger log = LoggerFactory.getLogger(CalendarEventController.class);

	
    //private final CalendarEventRepository repo;
	private final CalendarEventService eventService;
    private final MemberRepository memberRepo;

    public CalendarEventController(CalendarEventService eventService, MemberRepository memberRepo) {
        this.eventService =  eventService;
        this.memberRepo = memberRepo;
    }

    // ========================
    // 일정 조회: 기본 최근 7일, 로그인 유저 기준
    // ========================
    @GetMapping
    public List<CalendarEvent> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        LocalDate today = LocalDate.now();

        if (from == null) {
            from = today.minusDays(6); // 최근 7일 시작
        }
        if (to == null) {
            to = today; // 오늘까지
        }

        return eventService.list(currentUserId(), from, to);
    }
    
 // ✅ 일정 상세 조회 (id)
    @GetMapping("/{id}")
    public CalendarEvent get(@PathVariable Long id) {
        return eventService.get(currentUserId(), id);
    }

    
    // ========================
    // 일정 추가
    // ========================
    @PostMapping
    public CalendarEvent create(@RequestBody CalendarEvent body) {
        body.setId(null); // 프론트에서 보내는 ID 무시
        body.setUserId(currentUserId()); // 서버에서 로그인 유저 강제 세팅
        return eventService.create(currentUserId(),body);
    }

    // ========================
    // ✅ 일정 수정
    // ========================
    @PutMapping("/{id}")
    public CalendarEvent update(@PathVariable Long id, @RequestBody CalendarEvent body) {
        return eventService.update(currentUserId(), id, body);
    }
    
    // ========================
    // 일정 삭제
    // ========================
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eventService.delete(currentUserId(), id);
    }

    // ========================
    // private helper: 현재 로그인 유저 ID 가져오기
    // 테스트용 + JWT 겸용
    // ========================
    private Long currentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        log.error("AUTH = {}", auth);
        log.error("PRINCIPAL = {}", auth == null ? null : auth.getPrincipal());
        log.error("PRINCIPAL_CLASS = {}", (auth == null || auth.getPrincipal() == null) ? null : auth.getPrincipal().getClass());
        
        
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 필요");
        }

        Object p = auth.getPrincipal();

        // ✅ 익명 로그인 차단 (이거 없으면 "사용자 없음"으로 떨어짐)
        if (p instanceof String s && "anonymousUser".equals(s)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 필요");
        }

        // ✅ String이면 email로 보고 DB 조회
        if (p instanceof String email) {
            return memberRepo.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자 없음: " + email))
                    .getId();
        }

        // ✅ UserDetails 지원
        if (p instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            String email = ud.getUsername();
            return memberRepo.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자 없음: " + email))
                    .getId();
        }

        // ✅ Map principal 지원(네가 그렇게 넣는 구조일 때)
        if (p instanceof Map<?, ?> principal) {
            Object uidObj = principal.get("uid");
            if (uidObj instanceof Number) return ((Number) uidObj).longValue();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT에 uid 없음");
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "알 수 없는 사용자 타입: " + p.getClass());
    }


}
