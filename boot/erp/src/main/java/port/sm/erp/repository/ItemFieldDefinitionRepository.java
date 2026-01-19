package port.sm.erp.repository;
//DB(데이터베이스)에 접근하는 역할을 하는 Repository(저장소) 인터페이스입니다. 데이터베이스 사이에서 CRUD 작업을 담당
import java.util.List;//여러 결과를 담을때 사용
import java.util.Optional;//값이 있을수도 없을수도 있을때 사용

import org.springframework.data.jpa.repository.JpaRepository;

import port.sm.erp.entity.ItemFieldDefinition;

public interface ItemFieldDefinitionRepository extends JpaRepository<ItemFieldDefinition, Long> {
	
	Optional<ItemFieldDefinition> findByFieldKey(String fieldKey); //단건조회
	
	boolean existsByFieldKey(String fieldKey);//존재여부 확인
	
	//사용중인(또는 전체)정의 목록
	List<ItemFieldDefinition> findByUseYnOrderByIdAsc(String useYn);//사용 여부로 목록 조회 + 정렬

}
