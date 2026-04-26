package br.com.latanks.cidasdepilacao_api.configs;

import br.com.latanks.cidasdepilacao_api.filters.JWTFilter;
import br.com.latanks.cidasdepilacao_api.security.CustomAccessDeniedHandler;
import br.com.latanks.cidasdepilacao_api.security.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JWTFilter jwtFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Value("${api.base-url}")
    private String baseUrl;

    public SecurityConfig(JWTFilter jwtFilter, CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(baseUrl + "/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, baseUrl + "/appointment").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, baseUrl + "/appointment/my").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, baseUrl + "/user/my").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, baseUrl + "/user/{id}").hasAnyRole("USER", "ADMIN")

                        //.requestMatchers(HttpMethod.GET, baseUrl + "/appointment").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, baseUrl + "/appointment/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, baseUrl + "/appointment/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, baseUrl + "/appointment/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, baseUrl + "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, baseUrl + "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, baseUrl + "/user/{id}/promote").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
