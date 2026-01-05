package port.sm.erp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import port.sm.erp.entity.CalendarEvent;
import port.sm.erp.repository.CalendarEventRepository;

@RestController
//API를 만드는 콘트롤러 [매서드가 리턴하는 값이 json으로 자동변환돼서 프론트로 전달됨]
@RequestMapping("/events")//이콘트롤러의 공통주소를 정해줌
public class CalendarEventController {

	//리파지토리 주입 (DB 담당자 연결)
	private final CalendarEventRepository repo;
//컨트롤러(창구 직원)가 Repository(DB 담당자)에게 “조회해줘/저장해줘/삭제해줘” 시키는 구조	
	public CalendarEventController(CalendarEventRepository repo) {
		this.repo = repo;
	}
	
	
//제일 중요한 부분 지금 요청을 보낸 사람이 누구인지(userId)를 JWT토근에서 꺼내서 알려주는 함수
private Long currentUserId() {
Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//Spring Security가 로그인(토큰인증) 성공하면 이 요청은 누구 요청이다 를 SecurityContext에 넣어줍니다
//Principal 요청한 사용자 정보

@SuppressWarnings("unchecked")
Map<String, Object> principal = (Map<String, Object>)p;
//principal이 Object라서 Map으로 바꿔서 "uid"를 꺼내려고 하는 것
//UID는 주로 User ID(사용자 식별자) 또는 **Unique Identifier(고유 식별자)**의 약자
return ((Number) principal.get("uid")).longValue();
//uid를 꺼내서 롱으로 변환 
/*
Object 타입이라 바로 Long으로 쓸 수 없음
안전하게 처리하려면 공통 부모 타입인 Number 로 받는 것이 가장 안정적
DB용 id 타입이 Long 이기 떄문에
*/
	}

//일정조회 프론트가 이번달 일정을 전부 보여줄때
/*
핵심동작 
from ~ to 기간을 받고
현재 로그인한 userId만 넣어서 조회

sql로 비유하면
SELECT * FROM calendar_event
WHERE user_id = 내아이디
AND date BETWEEN from AND to;
*/
@GetMapping
public List<CalendarEvent> list(@RequestParam LocalDate from, @RequestParam LocalDate to){
	return repo.findByUserIdAndDateBetween(currentUserId(), from, to);
}
	

//일정추가
@PostMapping
public CalendarEvent create(@RequestBody CalendarEvent body) {
	body.setId(null);//프론트가 실수로 id를 보내도 무시하고 DB가 자동으로
//id를 만들게 하려는 안전장치
	body.setUserId(currentUserId());
//why userid는 프론트가 userId = 2이렇게 보내게 하면 다른 사람이  일정에 저장하는 
	//해킹이 가능함 
//userId는 절대 프론트에서 받지 말고 서버에서 토큰으로 강제세팅
	return repo.save(body);//DB에 insert 저장
}

//일정삭제
@DeleteMapping("/{id}")
public void delete(@PathVariable Long id) {
	CalendarEvent ev = repo.findById(id).orElseThrow();
	//id가 일정이 db에 있는지 찾음
	if(!ev.getUserId().equals(currentUserId())) {
		//내일정이 아니면 삭제 하면 안됨
		throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		//그래서 403 forbidden 반환
	}
	repo.delete(ev);//삭제 실행
}
	
	
	
	
	
	
	
}
