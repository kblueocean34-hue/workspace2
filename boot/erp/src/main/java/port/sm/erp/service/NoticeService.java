package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.NoticeCreateRequest;
import port.sm.erp.dto.NoticeDetailResponse;
import port.sm.erp.dto.NoticeListResponse;
import port.sm.erp.dto.NoticeUpdateRequest;
import port.sm.erp.entity.Member;
import port.sm.erp.entity.Notice;
import port.sm.erp.repository.MemberRepository;
import port.sm.erp.repository.NoticeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    //등록
    public Long createNotice(Long memberId, NoticeCreateRequest request) {
        if (memberId == null) {
            throw new IllegalArgumentException("Member ID가 null입니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 멤버가 존재하지 않습니다. id=" + memberId));

        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .isPinned(request.getIsPinned())
                .member(member)
                .build();

        noticeRepository.save(notice);
        return notice.getId();
    }

    /*public Long createNotice(Long memberId, NoticeCreateRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        Notice notice = Notice.builder()
.title(request.getTitle())
.content(request.getContent())
.isPinned(request.getIsPinned() != null && request.getIsPinned())
.member(member)
.build();
noticeRepository.save(notice);
return notice.getId();
    }*/

    //수정
    public void updateNotice(Long id, NoticeUpdateRequest request){
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지 없음"));
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setIsPinned(request.getIsPinned());
    }

    //삭제
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }
    //목록
    @Transactional(readOnly = true)
    public List<NoticeListResponse> getNoticeList(){
        return noticeRepository.findAllByOrderByIsPinnedDescCreatedAtDesc().stream()
                .map(this::toListDto).collect(Collectors.toList());
    }

    //상세
    public NoticeDetailResponse getNoticeDetail(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("공지사항 ID는 null일 수 없습니다.");
        }

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항이 존재하지 않습니다. id=" + id));

        return toDetailDto(notice);
    }
    /*public NoticeDetailResponse getNoticeDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(()->new RuntimeException("공지 없음"));
        notice.increaseViewCount();//조회수 증가
        return toDetailDto(notice);
    }*/

    //dto변환
    private NoticeListResponse toListDto(Notice notice) {
        return NoticeListResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .writer(notice.getMember().getUsername())
                .isPinned(notice.getIsPinned())
                .viewCount(notice.getViewCount())
                .createdAt(notice.getCreatedAt())
                .build();
    }

    private NoticeDetailResponse toDetailDto(Notice notice) {
        return NoticeDetailResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .writer(notice.getMember().getUsername())
                .isPinned(notice.getIsPinned())
                .viewCount(notice.getViewCount())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }

}
