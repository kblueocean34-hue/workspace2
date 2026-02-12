package port.sm.erp.controller;

import lombok.*;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.NoticeCreateRequest;
import port.sm.erp.dto.NoticeDetailResponse;
import port.sm.erp.dto.NoticeListResponse;
import port.sm.erp.dto.NoticeUpdateRequest;
import port.sm.erp.service.NoticeService;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor //📌 final 필드인 noticeService를 생성자 주입(DI)
public class NoticeController {

    private final NoticeService noticeService;
    /*
📌 공지사항 비즈니스 로직을 담당하는 Service 주입
📌 final + @RequiredArgsConstructor로 생성자 주입
    * */

    @GetMapping
    public List<NoticeListResponse> getList(){
        return noticeService.getNoticeList();
    }

    @GetMapping("/{id}")
    public NoticeDetailResponse getDetail(@PathVariable Long id){
        return noticeService.getNoticeDetail(id);
    }

    /*@PostMapping
    public Long create(@RequestParam Long memberId, @RequestBody NoticeCreateRequest request){
        return noticeService.createNotice(memberId, request);
    }
    @PostMapping
    public Long create(@RequestBody NoticeCreateRequest request) {
        return noticeService.createNotice(request.getMemberId(), request);
    }*/
    @PostMapping
    public Long create(@RequestBody NoticeCreateRequest request) {
        if (request.getMemberId() == null) {
            throw new IllegalArgumentException("memberId는 필수입니다.");
        }
        return noticeService.createNotice(request.getMemberId(), request);
    }


    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody NoticeUpdateRequest request){
        noticeService.updateNotice(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        noticeService.deleteNotice(id);
    }

}
