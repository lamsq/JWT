package com.example.jwt.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger l = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fch) throws ServletException, IOException {
        l.info("Request: Method={}, URI={}, RemoteAddr={}", req.getMethod(), req.getRequestURI(), req.getRemoteAddr());
        fch.doFilter(req, res);
        l.info("Response: Status={}", res.getStatus());
    }
}
