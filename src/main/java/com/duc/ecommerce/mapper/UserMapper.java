package com.duc.ecommerce.mapper;

import com.duc.ecommerce.dto.request.UserCreateRequest;
import com.duc.ecommerce.dto.response.UserResponse;
import com.duc.ecommerce.entity.*;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser (UserCreateRequest request);
    UserResponse toUserResponse(User user);

}
