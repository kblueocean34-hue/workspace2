package port.sm.erp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;//기본 crud기능 
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import port.sm.erp.entity.ItemFieldValue;

public interface ItemFieldValueRepository extends JpaRepository<ItemFieldValue, Long> {

    List<ItemFieldValue> findByItem_Id(Long itemId);

    void deleteByItem_Id(Long itemId);

    Optional<ItemFieldValue> findByItem_IdAndFieldDef_Id(Long itemId, Long fieldDefId);

    /**
     * 특정 itemId 들에 대한 extra field 값을 한 번에 가져올 때 (리스트 화면 최적화용)
     */
    @Query("""
        select v
        from ItemFieldValue v
        join fetch v.fieldDef d
        where v.item.id in :itemIds
    """)
    List<ItemFieldValue> findAllByItemIdsWithDef(@Param("itemIds") List<Long> itemIds);
}
