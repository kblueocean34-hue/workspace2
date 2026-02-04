package com.hbk.controller;

import com.hbk.dto.ProductResponse;
import com.hbk.entity.ProductEntity;
import com.hbk.repository.ProductRepository;
import com.hbk.storage.FileStorage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductController {

    private final ProductRepository repo;
    private final FileStorage fileStorage;

    // ✅ 목록
    @GetMapping
    public List<ProductResponse> list() {
        return repo.findAll().stream().map(ProductResponse::from).toList();
    }

    // ✅ 상세조회 (추가)
    @GetMapping("/{id}")
    public ProductResponse detail(@PathVariable Long id) {
        ProductEntity e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("상품 없음"));
        return ProductResponse.from(e);
    }

    // ✅ 등록 (multipart/form-data)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse create(
            @RequestParam @NotBlank String title,
            @RequestParam(required = false) String desc,
            @RequestParam @NotNull Integer price,
            @RequestParam(required = false) Integer primaryCategory,    // 카테고리 추가
            @RequestParam(required = false) Integer secondaryCategory,  // 카테고리 추가
            @RequestPart("image") MultipartFile image
    ) throws Exception {
        // 카테고리 검증
        if (primaryCategory == null || secondaryCategory == null) {
            throw new IllegalArgumentException("카테고리를 선택하세요.");
        }

        // 파일 저장 처리
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("이미지를 선택하세요.");
        }
        var stored = fileStorage.save(image);

        // 상품 저장
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

    // ✅ 수정 (추가)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse update(
            @PathVariable Long id,
            @RequestParam @NotBlank String title,
            @RequestParam(required = false) String desc,
            @RequestParam @NotNull Integer price,
            @RequestParam(required = false) Integer primaryCategory,    // 카테고리 필드
            @RequestParam(required = false) Integer secondaryCategory,  // 카테고리 필드
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws Exception {

        ProductEntity e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        // 이미지가 새로 들어오면 기존 파일 삭제 후 저장
        if (image != null && !image.isEmpty()) {
            // 기존 이미지 파일이 존재하는 경우 삭제
            fileStorage.deleteByPath(e.getImagePath());
            var stored = fileStorage.save(image);
            e.setImageUrl(stored.url());
            e.setImagePath(stored.filePath());
        }

        e.setTitle(title);
        e.setDesc(desc);
        e.setPrice(price);

        // 카테고리 필드 설정
        if (primaryCategory != null) {
            e.setPrimaryCategory(primaryCategory);
        }
        if (secondaryCategory != null) {
            e.setSecondaryCategory(secondaryCategory);
        }

        ProductEntity saved = repo.save(e);

        return ProductResponse.from(saved);
    }

    // ✅ 삭제 (DB 삭제 + 파일 삭제)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ProductEntity e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("상품 없음"));
        fileStorage.deleteByPath(e.getImagePath());
        repo.delete(e);
    }
}
