/*package port.sm.erp.controller;

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

import lombok.RequiredArgsConstructor;
import port.sm.erp.entity.CalendarEvent;
import port.sm.erp.repository.CalendarEventRepository;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class LombokCalendarController {

	private final CalendarEventRepository repo;
	
	private Long currentUserId() {
		Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> principal = (Map<String, Object>)p;
		return((Number) principal.get("uid")).longValue();
	}
	
	@GetMapping
	public List<CalendarEvent> list (
@RequestParam LocalDate from,
@RequestParam LocalDate to
	){
return repo.findByUserIdAndDateBetween(currentUserId(), from, to);		
	}

@PostMapping
public CalendarEvent create(@RequestBody CalendarEvent body) {
	body.setId(null);// 혹시 프론트가 id 보내도 무시
	body.setUserId(currentUserId());// userId는 서버가 토큰으로 강제 세팅
	return repo.save(body);
}

@DeleteMapping("/{id}")
public void delete(@PathVariable Long id) {
CalendarEvent ev = repo.findById(id).orElseThrow(()->new ResponseStatusException(
HttpStatus.NOT_FOUND,"Event not found"));
//내 일정이 아니면 삭제 금지
if(!ev.getUserId().equals(currentUserId())) {
throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your event");
}
repo.delete(ev);

}
	
	
	
	
}*/
