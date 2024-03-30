package com.devesta.blogify.security.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);
    private final ObjectMapper mapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpServletResponse.SC_UNAUTHORIZED);
        problemDetail.setTitle("Authentication Failure");
        problemDetail.setDetail(authException.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        logger.error("Authentication Failure - {}", problemDetail);
        mapper.writeValue(response.getOutputStream(), problemDetail);
    }

}
