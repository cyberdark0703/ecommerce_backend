package com.duc.ecommerce.configuration;

import com.duc.ecommerce.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                                // USER

                                // API công khai
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/user/create",
                                        "/user/login"
                                )
                                .permitAll()

                                // API chỉ dành cho ADMIN
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/user/read_one/**",
                                        "/user/readAll",
                                        "/user/readallcontainingignorecase/**",
                                        "/user/readallbyorderbynameasc",
                                        "/user/readalloffsetpage",
                                        "/user/readallcursorpage"
                                )
                                .hasRole("ADMIN")

                                .requestMatchers(HttpMethod.DELETE, "/user/delete/**")
                                .hasRole("ADMIN")

                                // API cập nhật tài khoản
                                .requestMatchers(HttpMethod.PUT, "/user/update/**")
                                .authenticated()

                                // PRODUCT - API công khai
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/product/read_one/**",
                                        "/product/findbypricebetweenandcategory",
                                        "/product/image/**"
                                )
                                .permitAll()

                                // PRODUCT - API cần quyền
                                .requestMatchers(HttpMethod.POST, "/product/create")
                                .hasAuthority("PRODUCT_CREATE")

                                .requestMatchers(HttpMethod.PUT,
                                        "/product/update/**",
                                        "/product/image/**")
                                .hasAuthority("PRODUCT_UPDATE")

                                .requestMatchers(HttpMethod.DELETE, "/product/delete/**")
                                .hasAuthority("PRODUCT_DELETE")

                                // CATEGORY - API cần quyền
                                .requestMatchers(HttpMethod.POST, "/category/create")
                                .hasAuthority("CATEGORY_CREATE")

                                // INVOICE - ADMIN được đọc tất cả hóa đơn
                                .requestMatchers(HttpMethod.GET, "/invoice/readAll")
                                .hasRole("ADMIN")

                                // INVOICE - các API khác yêu cầu đăng nhập
                                .requestMatchers("/invoice/**")
                                .authenticated()

                                // API khác
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("ACCESS DENIED HANDLER RUN");
                            System.out.println("BEFORE STATUS = " + response.getStatus());

                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                            System.out.println("AFTER STATUS = " + response.getStatus());
                        })
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    AuthenticationManager manager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            System.out.println("AUTHENTICATION ENTRY POINT RUN");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        };

    }
}
