package com.example.userservice.config;

import com.example.userservice.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.userservice.entity.Permission.*;
import static com.example.userservice.entity.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/api/v1/**",
                                        "/api/v1/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html"
                                )
                                .permitAll()
                                .requestMatchers("/api/v1/patient/**").hasAnyRole(ADMIN.name(), DOCTOR.name(), PATIENT.name())

                                .requestMatchers(GET, "/api/v1/patient/**").hasAnyAuthority(ADMIN_READ.name(), DOCTOR_READ.name(), PATIENT_READ.name())
                                .requestMatchers(POST, "/api/v1/patient/**").hasAnyAuthority(ADMIN_CREATE.name(), DOCTOR_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/patient/**").hasAnyAuthority(ADMIN_UPDATE.name(), DOCTOR_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/patient/**").hasAnyAuthority(ADMIN_DELETE.name(), DOCTOR_DELETE.name())

                                /*.requestMatchers("/api/v1/doctor/**").hasAnyRole(ADMIN.name(), DOCTOR.name())

                                .requestMatchers(GET, "/api/v1/doctor/**").hasAnyAuthority(ADMIN_READ.name(), DOCTOR_READ.name())
                                .requestMatchers(POST, "/api/v1/doctor/**").hasAnyAuthority(ADMIN_CREATE.name(), DOCTOR_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/doctor/**").hasAnyAuthority(ADMIN_UPDATE.name(), DOCTOR_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/doctor/**").hasAnyAuthority(ADMIN_DELETE.name(), DOCTOR_DELETE.name())
*/
                                /*.requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())

                                .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
                                .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())*/
                                .anyRequest()
                                .authenticated()
                )
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout ->
                                        logout.logoutUrl("/api/v1/auth/logout")
                                                .addLogoutHandler(logoutHandler)
                                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                                );

        return http.build();
    }
}
