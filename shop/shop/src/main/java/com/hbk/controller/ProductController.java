package com.hbk.controller;

import com.hbk.dto.ProductResponse;
import com.hbk.entity.ProductEntity;
import com.hbk.repository.ProductRepository;
import com.hbk.storage.FileStorage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository repo;
    private final FileStorage fileStorage;

    // ✅ 목록
    @GetMapping
    public List<ProductResponse> list() {
        return repo.findAll().stream().map(ProductResponse::from).toList();
    }

    // ✅ 등록 (multipart/form-data)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse create(
            @RequestParam @NotBlank String title,
            @RequestParam(required = false) String desc,
            @RequestParam @NotNull Integer price,
            @RequestPart("image") MultipartFile image
    ) throws Exception {

        var stored = fileStorage.save(image);

        ProductEntity saved = repo.save(ProductEntity.builder()
                .title(title)
                .desc(desc)
                .price(price)
                .imageUrl(stored.url())
                .imagePath(stored.filePath())
                .build());

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
