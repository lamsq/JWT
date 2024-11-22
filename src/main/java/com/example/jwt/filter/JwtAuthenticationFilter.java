package com.example.jwt.filter;

import com.example.jwt.service.UserDetailService;
import com.example.jwt.utils.JWTUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtils jwtu;
    private final UserDetailService uds;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse res, FilterChain fChain) throws ServletException, IOException {
        String h = request.getHeader("Authorization");
        if (h==null || !h.startsWith("Bearer ")) {
            fChain.doFilter(request, res);
            return;
        }
        String jwt = h.substring(7);
        String username = jwtu.extractUsername(jwt);

        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails = uds.loadUserByUsername(username);
            if (jwtu.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        fChain.doFilter(request, res);
    }
}
