package com.duc.ecommerce.dto.request;

import com.duc.ecommerce.validation.annotation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @NotBlank (message = "name cannot null")
    String name;
    @StrongPassword (message = "invalid password")
    String password;
}
