package com.duc.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    @NotBlank (message = "name cannot null")
    String name;
    @NotBlank (message = "category_name cannot null")
    String category_name;
    @Positive (message = "price must > 0")
    int price;
}
