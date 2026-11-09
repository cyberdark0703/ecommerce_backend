package com.duc.ecommerce.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum Role {
    ADMIN(Set.of(
            Permission.PRODUCT_DELETE,
            Permission.PRODUCT_UPDATE,
            Permission.PRODUCT_CREATE,
            Permission.CATEGORY_CREATE
    )),

    USER(Set.of());

    Set<Permission> permissions;
}