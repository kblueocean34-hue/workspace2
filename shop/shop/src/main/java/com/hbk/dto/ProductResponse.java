package com.hbk.dto;

import com.hbk.entity.ProductEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String title;
    private String desc;
    private Integer price;
    private String imageUrl;
    private Integer primaryCategory;
    private Integer secondaryCategory;

    // 기본값 설정
    private static final Integer DEFAULT_PRIMARY_CATEGORY = 1;  // 기본 Primary 카테고리 ID
    private static final Integer DEFAULT_SECONDARY_CATEGORY = 11; // 기본 Secondary 카테고리 ID

    public static ProductResponse from(ProductEntity e) {
        // primaryCategory와 secondaryCategory가 null일 경우 기본값으로 설정
        return ProductResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .desc(e.getDesc())
                .price(e.getPrice())
                .imageUrl(e.getImageUrl())
                .primaryCategory(e.getPrimaryCategory() != null ? e.getPrimaryCategory() : DEFAULT_PRIMARY_CATEGORY)
                .secondaryCategory(e.getSecondaryCategory() != null ? e.getSecondaryCategory() : DEFAULT_SECONDARY_CATEGORY)
                .build();
    }
}
