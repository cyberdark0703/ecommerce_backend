package com.duc.ecommerce.service;

import com.duc.ecommerce.dto.request.ProductCreateRequest;
import com.duc.ecommerce.dto.response.ProductResponse;
import com.duc.ecommerce.entity.Category;
import com.duc.ecommerce.entity.Product;
import com.duc.ecommerce.exception.EcommerceException;
import com.duc.ecommerce.exception.ErrorCode;
import com.duc.ecommerce.mapper.ProductMapper;
import com.duc.ecommerce.repository.CategoryRepository;
import com.duc.ecommerce.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductService {
    @Autowired
    ProductRepository repository;
    ProductMapper mapper;
    CategoryRepository categoryRepository;

    public ProductResponse create (ProductCreateRequest request){
        if (repository.existsByNameContainingIgnoreCase(request.getName())){
            throw new EcommerceException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }
        Product product = mapper.toProduct(request);
        Category category = categoryRepository.findByName(request.getCategory_name());
        product.setCategory(category);

        repository.save(product);

        ProductResponse response = mapper.toProductResponse(product);
        String id = "product_" + product.getId();
        response.setId(id);
        return response;
    }
    //public ProductResponse update (ProductCreateRequest request){}
    //Những chức năng cụ thể hơn
    //Tìm product trong khoảng giữa 2 giá và tên category tương ứng
    public List<ProductResponse> findByPriceBetweenAndCategory (BigDecimal minPrice,
                                                                BigDecimal maxPrice,
                                                                String categoryName){
        List<ProductResponse> responses = repository.findByPriceBetweenAndCategory(minPrice,maxPrice,categoryName)
                .stream().map(product -> {
                    ProductResponse response = mapper.toProductResponse(product);
                    String id = "product_" + product.getId();
                    response.setId(id);
                    return response;
                })
                .toList();

        return responses;
    }

    public ProductResponse read (String id){
        Product product = repository.findById(id).orElseThrow(()-> new EcommerceException(ErrorCode.USER_NOT_FOUND));

        ProductResponse response = mapper.toProductResponse(product);
        String id_response = "product_"+product.getId();
        response.setId(id_response);
        return response;
    }


}
