// ✅ ApprovalDocRepository.java
package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import port.sm.erp.entity.ApprovalDoc;

import java.util.List;

public interface ApprovalDocRepository extends JpaRepository<ApprovalDoc, Long> {

    @Query("""
        select d
        from ApprovalDoc d
        left join fetch d.drafter dr
        left join fetch d.approver ap
        order by d.id desc
    """)
    List<ApprovalDoc> findList();

    @Query("""
        select d
        from ApprovalDoc d
        left join fetch d.drafter dr
        left join fetch d.approver ap
        where d.id = :id
    """)
    ApprovalDoc findDetail(@Param("id") Long id);

    // ✅ "내 기안문서"용 (drafterId로 조회)
    @Query("""
        select d
        from ApprovalDoc d
        left join fetch d.drafter dr
        left join fetch d.approver ap
        where dr.id = :drafterId
        order by d.id desc
    """)
    List<ApprovalDoc> findMyDrafts(@Param("drafterId") Long drafterId);
}
