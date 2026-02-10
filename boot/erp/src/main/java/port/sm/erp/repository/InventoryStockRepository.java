package port.sm.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import port.sm.erp.entity.InventoryStock;


import java.awt.print.Pageable;
import java.util.List;
//구현체 없이 “기능 목록”만 선언
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {

    boolean existsByItem_Id(Long itemId);
    //해당 품목(itemId)의 재고가 존재하는지 확인

    //커스텀 검색쿼리
    @Query("""
            select s
            from InventoryStock s
            join s.item i
            where lower(i.itemCode) like lower(concat('%', :q, '%'))
            or lower(i.itemName) like lower(concat('%', :q, '%'))
            """)
    Page<InventoryStock> search(@Param("q") String q, Pageable pageable);
    /*
   @Param("q") : 쿼리의 :q와 연결
   Pageable : 페이지/사이즈 설정
   Page<InventoryStock> : 페이징된 결과 반환
    */
}
/*
select s : 재고 엔티티 반환
from InventoryStock s
join s.item i : 재고에 연결된 품목(item) 테이블 조인
where lower 대소문자 무시 검색 (i.itemCode) like lower(concat('%', :q, '%'))  포함 검색
or lower(i.itemName) like lower(concat('%', :q, '%')) 검색어 파라미터
* */
