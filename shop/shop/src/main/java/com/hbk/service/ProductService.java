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

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return repo.findAll().stream().map(ProductResponse::from).toList();
    }

    @Transactional
    public ProductResponse create(String title, String desc, Integer price, MultipartFile image) throws Exception {
        // ✅ 기본 검증
        if (title == null || title.isBlank()) throw new IllegalArgumentException("상품명은 필수입니다.");
        if (price == null || price <= 0) throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        if (image == null || image.isEmpty()) throw new IllegalArgumentException("이미지를 선택하세요.");

        // ✅ 이미지 저장
        var stored = fileStorage.save(image);

        // ✅ DB 저장
        ProductEntity saved = repo.save(ProductEntity.builder()
                .title(title)
                .desc(desc)
                .price(price)
                .imageUrl(stored.url())
                .imagePath(stored.filePath())
                .build());

        return ProductResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        ProductEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + id));

        // ✅ 파일 삭제 → DB 삭제
        fileStorage.deleteByPath(e.getImagePath());
        repo.delete(e);
    }
}