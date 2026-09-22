package com.duc.ecommerce.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class InvoiceResponse {
    String id;
    String user_name;
    List<ProductResponse> products;
    int total_price;
    LocalDateTime born;

}
