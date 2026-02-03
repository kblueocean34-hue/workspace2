package com.hbk.service;

import com.hbk.dto.ProductResponse;
import com.hbk.entity.ProductEntity;
import com.hbk.repository.ProductRepository;
import com.hbk.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final FileStorage fileStorage;

    // ✅ 목록 조회
    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return repo.findAll().stream().map(ProductResponse::from).toList();
    }

    // ✅ 단일 조회
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        ProductEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + id));
        return ProductResponse.from(e);
    }

    // ✅ 생성
    @Transactional
    public ProductResponse create(String title, String desc, Integer price,
                                  Integer primaryCategory, Integer secondaryCategory,
                                  MultipartFile image) throws Exception {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("상품명은 필수입니다.");
        if (price == null || price <= 0) throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        if (image == null || image.isEmpty()) throw new IllegalArgumentException("이미지를 선택하세요.");

        var stored = fileStorage.save(image);

        ProductEntity saved = repo.save(ProductEntity.builder()
                .title(title)
                .desc(desc)
                .price(price)
                .imageUrl(stored.url())
                .imagePath(stored.filePath())
                .primaryCategory(primaryCategory)
                .secondaryCategory(secondaryCategory)
                .build());

        return ProductResponse.from(saved);
    }

    // ✅ 수정
    @Transactional
    public ProductResponse update(Long id, String title, String desc, Integer price,
                                  Integer primaryCategory, Integer secondaryCategory,
                                  MultipartFile image) throws Exception {
        ProductEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + id));

        if (title != null && !title.isBlank()) e.setTitle(title);
        if (desc != null) e.setDesc(desc);
        if (price != null && price > 0) e.setPrice(price);

        e.setPrimaryCategory(primaryCategory);
        e.setSecondaryCategory(secondaryCategory);

        // 이미지가 새로 들어오면 기존 파일 삭제 후 저장
        if (image != null && !image.isEmpty()) {
            fileStorage.deleteByPath(e.getImagePath());
            var stored = fileStorage.save(image);
            e.setImageUrl(stored.url());
            e.setImagePath(stored.filePath());
        }

        ProductEntity saved = repo.save(e);
        return ProductResponse.from(saved);
    }

    // ✅ 삭제
    @Transactional
    public void delete(Long id) {
        ProductEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + id));

        fileStorage.deleteByPath(e.getImagePath());
        repo.delete(e);
    }
}
