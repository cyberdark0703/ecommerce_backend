package com.duc.ecommerce.service;

import com.duc.ecommerce.dto.request.CategoryCreateRequest;
import com.duc.ecommerce.dto.response.CategoryResponse;
import com.duc.ecommerce.entity.Category;
import com.duc.ecommerce.entity.Product;
import com.duc.ecommerce.exception.EcommerceException;
import com.duc.ecommerce.exception.ErrorCode;
import com.duc.ecommerce.mapper.CategoryMapper;
import com.duc.ecommerce.repository.CategoryRepository;
import com.duc.ecommerce.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryService {
    @Autowired
    CategoryRepository repository;
    CategoryMapper mapper;
    ProductRepository productRepository;

    public CategoryResponse create (CategoryCreateRequest request){
        if (repository.existsByName(request.getName())){
            throw new EcommerceException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = mapper.toCategory(request);

        /*List<Product> productsList = productRepository.findAll()
                .stream()
                .filter(product->product.getCategory() != null
                        && product.getCategory().getName().equals(category.getName()))
                .toList();

        category.setProducts(productsList);*/

        repository.save(category);

        CategoryResponse response = mapper.toCategoryResponse(category);
        String id = "category_" + category.getId();
        response.setId(id);

        /*List<String> products = category.getProducts()
                .stream()
                .map(Product::getName)
                .toList();

        response.setProducts_name(products);*/

        return response;
    }

}
