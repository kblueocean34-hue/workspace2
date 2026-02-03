package com.hbk.dto;

import com.hbk.entity.ProductEntity;
import lombok.*;

@Getter @Setter
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

    public static ProductResponse from(ProductEntity e) {
        return ProductResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .desc(e.getDesc())
                .price(e.getPrice())
                .imageUrl(e.getImageUrl())
                .primaryCategory(e.getPrimaryCategory())
                .secondaryCategory(e.getSecondaryCategory())
                .build();
    }
}
