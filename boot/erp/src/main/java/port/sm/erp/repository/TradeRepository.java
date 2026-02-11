package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import port.sm.erp.entity.Trade;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    // ✅ 상세: customer + tradeLines + item까지 한번에 로딩
    @Query("""
        select distinct t
        from Trade t
        left join fetch t.customer c
        left join fetch t.tradeLines tl
        left join fetch tl.item i
        where t.id = :id
    """)
    Trade findDetail(@Param("id") Long id);

    // ✅ 목록: customer만 fetch (중복 방지용 distinct 추가)
    @Query("""
        select distinct t
        from Trade t
        left join fetch t.customer c
        order by t.id desc
    """)
    List<Trade> findListWithCustomer();
}
