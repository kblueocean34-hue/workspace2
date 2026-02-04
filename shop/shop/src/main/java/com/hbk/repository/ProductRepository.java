package com.hbk.repository;

import com.hbk.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // 1차 카테고리로 상품 리스트 조회
    List<ProductEntity> findByPrimaryCategory(Integer primaryCategory);

    // 2차 카테고리로 상품 리스트 조회
    List<ProductEntity> findBySecondaryCategory(Integer secondaryCategory);

    // 1차 카테고리와 2차 카테고리로 상품 리스트 조회
    List<ProductEntity> findByPrimaryCategoryAndSecondaryCategory(Integer primaryCategory, Integer secondaryCategory);

    // 예시: 가격 범위로 상품 조회
    List<ProductEntity> findByPriceBetween(Integer minPrice, Integer maxPrice);

    // 커스텀 JPQL 쿼리 예시
    @Query("SELECT p FROM ProductEntity p WHERE p.primaryCategory = :primaryCategory AND p.secondaryCategory = :secondaryCategory")
    List<ProductEntity> findByCategories(Integer primaryCategory, Integer secondaryCategory);
}
