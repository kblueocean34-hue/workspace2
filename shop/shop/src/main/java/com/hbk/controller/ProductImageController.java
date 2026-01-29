package com.hbk.controller;

import com.hbk.dto.ProductImageDto;
import com.hbk.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;


    @GetMapping("/api/product-images")
    public List<ProductImageDto> getProductImages(){
        return productImageService.getRandomWebImages();
    }


}
