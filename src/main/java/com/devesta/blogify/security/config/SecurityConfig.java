package com.devesta.blogify.security.config;

import com.devesta.blogify.security.jwt.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider provider;
    private final AuthEntryPoint authenticationEntryPoint;
    private final PasswordEncoder passwordEncoder;

    private static final String[] GENERAL_WHITELIST = {
            "/api/authentication/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/h2-console/**",
    };

    private static final String[] GET_WHITELIST = {
            "/api/v1/comments/{commentId}",
            "/api/v1/comments/{postId}/{commentId}/replies",
            "/api/v1/communities",
            "/api/v1/communities/{cid}/posts",
            "/api/v1/communities/{cid}",
            "/api/v1/communities/tags/{tagName}",
            "/api/v1/users/{uid}",
            "/api/v1/users",
            "/api/v1/users/{username}",
            "/api/v1/users/{uid}/posts",
            "/api/v1/users/{uid}/communities",
            "/api/v1/users/{uid}/comments",
            "/api/v1/posts/{pid}",
            "/api/v1/posts/{pid}/comments",
            "/api/v1/search/posts/{title}",
            "/api/v1/search/people/{username}",
            "/api/v1/search/communities/{name}",
            "/api/v1/search/comment/{body}"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> {
                    sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(HttpMethod.GET,GET_WHITELIST).permitAll()
                                .requestMatchers(GENERAL_WHITELIST).permitAll()
                                .anyRequest().authenticated()
                )
                .authenticationProvider(provider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                );

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsManager() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("root123"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("root123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
