package com.duc.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceCreateRequest {
    @NotBlank (message = "user_name cannot null")
    String user_name;
    List<String> products_name;
}
