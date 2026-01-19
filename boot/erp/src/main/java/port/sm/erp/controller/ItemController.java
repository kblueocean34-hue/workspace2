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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inv/items")
@CrossOrigin(origins = "http://localhost:5173") // 프론트 주소 맞게 수정
public class ItemController {
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
}
