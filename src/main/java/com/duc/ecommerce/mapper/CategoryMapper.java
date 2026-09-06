package com.duc.ecommerce.mapper;

import com.duc.ecommerce.dto.request.CategoryCreateRequest;
import com.duc.ecommerce.dto.response.CategoryResponse;
import com.duc.ecommerce.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreateRequest request);
    CategoryResponse toCategoryResponse(Category category);
}
