package com.duc.ecommerce.mapper;

import com.duc.ecommerce.dto.request.ProductCreateRequest;
import com.duc.ecommerce.dto.response.ProductResponse;
import com.duc.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring")
public interface ProductMapper {
    Product toProduct (ProductCreateRequest request);

    @Mapping(source = "category.name",target = "category_name")
    ProductResponse toProductResponse(Product product);
}
