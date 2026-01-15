package port.sm.erp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import port.sm.erp.entity.CalendarEvent;

//sql을 직접 안써도 저장,조회,삭제를 할수 있게 해주는 JPA인터 페이스
//DB의 calendar_event 테이블을 자바 코드로 다루기 위한 창구
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long>{
List<CalendarEvent> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to);	
	
Optional<CalendarEvent> findByIdAndUserId(Long id, Long userId);

List<CalendarEvent> findByUserIdAndDateBetweenOrderByDateAsc(Long userId, LocalDate from, LocalDate to);

//List<CalendarEvent> findSharedWithUser(Long userId, LocalDate from, LocalDate to);
@Query("""
		  select distinct e
		  from CalendarEvent e
		  join e.sharers s
		  where s = :userIdStr
		    and e.date between :from and :to
		  order by e.date asc
		""")
		List<CalendarEvent> findSharedWithUser(
		        @Param("userIdStr") String userIdStr,
		        @Param("from") LocalDate from,
		        @Param("to") LocalDate to
		);


	
}
//핵심은 이한줄 extends JpaRepository<CalendarEvent, Long> 이 모든 기능이 자동으로 생긴다
/*
save()	데이터 저장
findById()	ID로 조회
findAll()	전부 조회
delete()	삭제
count()	개수

spring의 규칙 매서드 이름이 sql문장이다

find      → 조회
ByUserId  → user_id = ?
AndDateBetween → date BETWEEN ? AND ?

sql

SELECT * 
FROM calendar_event
WHERE user_id = ?
AND date BETWEEN ? AND ?;

이걸

repo.save(event);                     // 저장
repo.findByUserIdAndDate(...);        // 특정 날짜 일정 조회
repo.findByUserIdAndDateBetween(...); // 한 달 일정 조회
repo.delete(event);  //삭제

비유로 정리
은행 창구 직원 : CalendarEventRepository
고객 요청서 : 메서드 이름
직원이 알아서 처리 : Spring이 SQL 생성
*/