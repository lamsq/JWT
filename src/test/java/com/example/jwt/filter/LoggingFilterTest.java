package com.example.jwt.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.*;
import jakarta.servlet.FilterChain;
import static org.mockito.Mockito.*;

class LoggingFilterTest {
    private LoggingFilter lf;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lf = new LoggingFilter();
    }

    @Test
    void testDoFilterInternal() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain mfc = mock(FilterChain.class);
        req.setRequestURI("/test-endpoint");
        req.setMethod("GET");
        lf.doFilterInternal(req, res, mfc);
        verify(mfc, times(1)).doFilter(req, res);
    }
}
