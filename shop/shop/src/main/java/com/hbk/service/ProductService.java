package com.hbk.service;

import com.hbk.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service //이 클래스는 ‘비즈니스 로직 담당’ 이라고 스프링에게 알려주는 표시
public class ProductImageService {

//상품 이미지 종류 목록 static → 클래스 하나당 한 번만 inal → 값 변경 불가  고정된 이미지 키 목록
    private static final List<String> IMAGE_KEYS =
            List.of("p1", "p2","p3", "p4");

    public List<ProductResponse> getRandomWebImages(){
        List<ProductResponse> result = new ArrayList<>();

        for (String key: IMAGE_KEYS){
            // seed를 key + 랜덤값으로 줘서 매번 다른 이미지
            int seed = ThreadLocalRandom.current().nextInt(1, 10000);
//랜덤 seed 만들기 (핵심 ⭐) 이미지 뽑기용 랜덤 번호
            String url = String.format(
             "https://picsum.photos/seed/%s-%d/400/300", key, seed
            );
            result.add(new ProductResponse(key, url));
            //DTO로 묶어서 리스트에 추가
        }
        return result;
    }
}
