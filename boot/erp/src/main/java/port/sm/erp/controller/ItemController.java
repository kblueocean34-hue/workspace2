package port.sm.erp.controller;

import org.springframework.data.domain.Page;
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

import lombok.RequiredArgsConstructor;
import port.sm.erp.dto.ItemRequest;
import port.sm.erp.dto.ItemResponse;
import port.sm.erp.service.ItemService;

@RestController //아이템(Item)을 조회·등록·수정·삭제(CRUD) 하는 API 창구입니다.
//프론트엔드(React, Vue 등)에서 요청을 보내면, 이 컨트롤러가 받아서 ItemService에 일을 시킵니다.
/*
이 클래스가 REST API 컨트롤러임을 의미 반환값을 JSON 형태로 자동 변환해서 응답
📌 HTML 페이지 반환 ❌ 📌 데이터(JSON) 반환 ⭕
*/
@RequiredArgsConstructor
/*final이 붙은 필드를 자동으로 생성자 주입 */
@RequestMapping("/api/inv/items")
@CrossOrigin(origins = "http://localhost:5173") // 프론트 주소 맞게 수정
public class ItemController {
	
/*실제 비즈니스 로직은 ItemService가 담당 컨트롤러는 요청 → 서비스 호출 → 결과 반환만 담당
역활분리
*/	
	 private final ItemService itemService;

	    /**
	     * 목록 조회
	     * 예) /api/inv/items?q=볼트&includeStopped=false&page=0&size=10&sortKey=itemCode&dir=asc
	     */
	    @GetMapping
	    public Page<ItemResponse> list(
@RequestParam(name = "q", required = false) String q,
@RequestParam(name = "includeStopped", defaultValue = "false") boolean includeStopped,
@RequestParam(name = "page", defaultValue = "0") int page,
@RequestParam(name = "size", defaultValue = "10") int size,
@RequestParam(name = "sortKey", required = false) String sortKey,
@RequestParam(name = "dir", defaultValue = "asc") String dir
	    ) {
	        return itemService.list(q, includeStopped, page, size, sortKey, dir);
	    }
	    
	    

	    /** 단건 조회 */
	    @GetMapping("/{id}")
	    public ItemResponse get(@PathVariable Long id) {
	        return itemService.get(id);
	    }

	    /** 등록 */
	    @PostMapping
	    public ItemResponse create(@RequestBody ItemRequest req) {
	        return itemService.create(req);
	    }

	    /** 수정 */
	    @PutMapping("/{id}")
	    public ItemResponse update(@PathVariable Long id, @RequestBody ItemRequest req) {
	        return itemService.update(id, req);
	    }

	    /** 삭제 */
	    @DeleteMapping("/{id}")
	    public void delete(@PathVariable Long id) {
	        itemService.delete(id);
	    }
	    
/*
@PathVariable 이란?
URL 경로(path)에 들어있는 값을 꺼내서 변수에 넣어주는 어노테이션
*/	    
}
