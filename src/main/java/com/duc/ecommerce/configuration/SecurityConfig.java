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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception{
        return http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/user/update/**")
                        .authenticated()

                        .requestMatchers("/user/**")
                        .permitAll()

                        .requestMatchers("/product/create")
                        .hasAuthority("PRODUCT_CREATE")

                        .requestMatchers("/product/update/**")
                        //.hasRole("ADMIN")
                        .hasAuthority("PRODUCT_UPDATE")

                        .requestMatchers("/product/delete/**")
                        //.hasRole("ADMIN")
                        .hasAuthority("PRODUCT_DELETE")

                        .anyRequest()
                        .authenticated())
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
    AuthenticationManager manager (AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) ->{
            System.out.println("AUTHENTICATION ENTRY POINT RUN");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        };

    }
}
