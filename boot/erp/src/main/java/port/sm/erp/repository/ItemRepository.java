package port.sm.erp.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import port.sm.erp.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item>{

Optional<Item> findByItemCode(String itemCode);//item_code컬럼이 같은 데이터를 조회 값이 없을수도 있으니까 null방지
//품목코드 존재여부 확인
boolean existsByItemCode(String itemCode);	

//조회할때 편하게 하기위해 쿼리를 만듬
@Query("""
select i from Item i
where (:includeStopped = true or i.useYn = 'Y')
  and ((:q is null) or (:q = '') 
       or lower(i.itemCode) like lower(concat('%', :q, '%'))
       or lower(i.itemName) like lower(concat('%', :q, '%'))
       or lower(i.barcode) like lower(concat('%', :q, '%')))
""")
Page<Item> search(@Param("q") String q, @Param("includeStopped") boolean includeStopped, Pageable pageable);

}

//JpaSpecificationExecutor<Item> 동적 검색 조건을 만들때 사용
//복잡한 검색 화면에 유용
//JpaRepository CRUD기능제공 save()[저장], findById([ID로 조회]), findAll()[전체조회], deleteById()[삭제]
//<Item[엔티티타입], Long[PK(ID)타입>