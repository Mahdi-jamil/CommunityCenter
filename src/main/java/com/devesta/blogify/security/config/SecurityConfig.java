package com.devesta.blogify.security.config;

import com.devesta.blogify.security.jwt.JwtAuthFilter;
import com.devesta.blogify.security.jwt.JwtService;
import com.devesta.blogify.security.jwt.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;


@EnableWebSecurity
@Configuration
public class SecurityConfig {


    private final AuthenticationProvider provider;
    private final PasswordEncoder passwordEncoder;
    private final LogoutHandler logoutHandler;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthEntryPoint authEntryPoint;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    private static final String[] GENERAL_WHITELIST = {
            "/api/authentication/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/h2-console/**",
            "/error",
            "/error/accessDenied.html"
    };

    private static final String[] GET_WHITELIST = {
            "/api/v1/comments/{commentId}",
            "/api/v1/comments/{postId}/{commentId}/replies",

            "/api/v1/communities",
            "/api/v1/communities/{cid}/posts",
            "/api/v1/communities/{cid}",
            "/api/v1/communities/{cid}/icon",
            "/api/v1/communities/tags/{tagName}",

            "/api/v1/users/{uid}",
            "/api/v1/users",
            "/api/v1/users/{uid}/profile",
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

    public SecurityConfig(AuthenticationProvider provider, PasswordEncoder passwordEncoder, LogoutHandler logoutHandler, JwtService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository, AccessDeniedHandlerImpl accessDeniedHandler, AuthEntryPoint authEntryPoint) {
        this.provider = provider;
        this.passwordEncoder = passwordEncoder;
        this.logoutHandler = logoutHandler;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtService, userDetailsService, tokenRepository, handlerExceptionResolver);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(HttpMethod.GET, GET_WHITELIST).permitAll()
                                .requestMatchers(GENERAL_WHITELIST).permitAll()
                                .requestMatchers("/api/v1/revoke_tokens").hasAuthority("ADMIN")
                                .anyRequest().authenticated()
                )
                .authenticationProvider(provider)
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout((logout) ->
                        logout
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                                .logoutUrl("/api/authentication/logout")
                ).exceptionHandling((exception) ->
                        exception
                                .accessDeniedHandler(accessDeniedHandler) // 403
                                .authenticationEntryPoint(authEntryPoint) // 401
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
