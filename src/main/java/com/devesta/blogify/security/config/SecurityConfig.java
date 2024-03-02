package com.devesta.blogify.security.config;

import com.devesta.blogify.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final AuthenticationProvider provider;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] AUTH_WHITELIST = {
            "/api/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/hello",
            "/api/accessDenied"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> {
                    sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers("/hello/t1").hasAnyAuthority("USER")
                                .requestMatchers("/hello/t2").hasAnyAuthority("ADMIN")
                                .anyRequest().authenticated()
                )
                .authenticationProvider(provider)
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(new CustomAccessDeniedHandler())
                                .accessDeniedPage("/api/accessDenied")// todo
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    /*  For test purposes
    @Bean
    public UserDetailsService userDetailsManager() {
                UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("root"))
                .roles("user")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("root"))
                .roles("admin")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
   */


}
