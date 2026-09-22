package com.duc.ecommerce.security;

import com.duc.ecommerce.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetails implements UserDetails {
    User user;

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        user.getRole().getPermissions().forEach(permission ->
                authorities.add(
                        new SimpleGrantedAuthority(permission.name())
                )
        );

        return authorities;
    }
}
