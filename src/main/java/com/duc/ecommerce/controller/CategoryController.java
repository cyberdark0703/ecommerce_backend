package com.duc.ecommerce.controller;

import com.duc.ecommerce.dto.request.CategoryCreateRequest;
import com.duc.ecommerce.dto.response.CategoryResponse;
import com.duc.ecommerce.service.CategoryService;
import lombok.AccessLevel;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@FieldDefaults (level = AccessLevel.PRIVATE)
public class CategoryController {
    @Autowired
    CategoryService service;

    @PostMapping("/create")
    CategoryResponse create (@RequestBody CategoryCreateRequest request){
        return service.create(request);
    }
}
