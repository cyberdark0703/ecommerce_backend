package com.duc.ecommerce.dto.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class UserLoginRequest {
    String name;
    String password;
}
