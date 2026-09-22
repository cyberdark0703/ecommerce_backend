package com.duc.ecommerce.controller;

import com.duc.ecommerce.dto.request.ProductCreateRequest;
import com.duc.ecommerce.dto.request.ProductUpdateRequest;
import com.duc.ecommerce.dto.response.ProductResponse;
import com.duc.ecommerce.service.ProductService;
import lombok.AccessLevel;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping ("/product")
@FieldDefaults (level = AccessLevel.PRIVATE)
public class ProductController {
    @Autowired
    ProductService service;

    @PostMapping("/create")
    ProductResponse create (@RequestBody ProductCreateRequest request){
        return service.create(request);
    }

    @GetMapping("/read_one/{id}")
    ProductResponse read (@PathVariable String id){
        return service.read(id);
    }

    @GetMapping("/findbypricebetweenandcategory")
    List<ProductResponse> findByPriceBetweenAndCategory (@RequestParam BigDecimal minPrice,
                                                         @RequestParam BigDecimal maxPrice,
                                                         @RequestParam String categoryName){
        return service.findByPriceBetweenAndCategory(minPrice,maxPrice,categoryName);
    }
    @PutMapping("/update/{id}")
    ProductResponse update (@PathVariable String id, @RequestBody ProductUpdateRequest request){
        return service.update(id,request);
    }

    @DeleteMapping("/delete/{id}")
    void delete(@PathVariable String id){
         service.delete(id);
    }

}
