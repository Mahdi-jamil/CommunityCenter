package com.devesta.blogify.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpServletResponse.SC_FORBIDDEN);
        problemDetail.setTitle("Access Denied");
        problemDetail.setDetail(accessDeniedException.getMessage() + ": you don't have permission to access this resource");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        LoggerFactory.getLogger("AuthenticationHandler").error("Access Denied - {}", problemDetail);
        new ObjectMapper().writeValue(response.getOutputStream(), problemDetail);
    }
}

