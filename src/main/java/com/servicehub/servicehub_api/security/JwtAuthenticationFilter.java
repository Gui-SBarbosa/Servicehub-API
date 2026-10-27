package com.servicehub.servicehub_api.security;

import com.servicehub.servicehub_api.model.UserAccount;
import com.servicehub.servicehub_api.repository.UserAccountRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService; private final UserAccountRepository users;
    public JwtAuthenticationFilter(JwtService jwtService, UserAccountRepository users) { this.jwtService = jwtService; this.users = users; }
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) { chain.doFilter(request, response); return; }
        try {
            String email = jwtService.subject(header.substring(7));
            UserAccount user = users.findByEmail(email).filter(UserAccount::isActive).orElse(null);
            if (user != null) SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))));
        } catch (JwtException | IllegalArgumentException ignored) { SecurityContextHolder.clearContext(); }
        chain.doFilter(request, response);
    }
}
