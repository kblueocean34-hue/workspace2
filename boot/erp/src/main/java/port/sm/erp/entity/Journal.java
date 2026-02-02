package port.sm.erp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity //이 클래스는 DB 테이블과 연결되는 객체다
@Table(name = "journals")//👉 이 엔티티가 어떤 테이블을 쓰는지 지정
@Getter @Setter //하지만 Lombok 덕분에 자동으로 만들어줌 롬복을 사용하지 않으면 따로 설정
public class Journal {

    @Id //기본키(PK) 설정
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "journal_seq")
    //👉 ID를 자동으로 만들어 주세요 주로 Oracle, PostgreSQL에서 많이 씀
    @SequenceGenerator(
name = "journal_seq", sequenceName = "SEQ_JOURNAL",  allocationSize = 1
    )
    /*
 name : jpa내부에서 부르는 이름
 sequenceName : DB에 실제 존재하는 시퀀스 이름
 allocationSize : 몇개씩 미리 가져올지 (1이면 하나씩)
    * */
    private Long id;

    private LocalDate journalDate;
/*전표날짜 */
    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL)
/*@OneToMany 1대 다관계 journal 1개 JournalLine 여러개
cascade = CascadeType.ALL 부모가 바뀌면 자식도 같이 처리
Journal은 전표 헤더(머리)
JournalLine은 전표 상세(몸통)
* */    
    private List<JournalLine> lines = new ArrayList<>();
}
