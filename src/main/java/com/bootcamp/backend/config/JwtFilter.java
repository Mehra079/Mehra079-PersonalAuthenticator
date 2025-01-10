package com.bootcamp.backend.config;

import com.bootcamp.backend.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = "/api/*")
public class JwtFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // List of public endpoints that don't require authentication
        if (path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/register") ||
                path.startsWith("/api/auth/send-otp") ||
                path.startsWith("/api/auth/verify-otp") ||
                path.startsWith("/api/auth/forgot-password"))
        {
            chain.doFilter(request, response);
            return;
        }

        // Protected endpoints: Validate JWT token
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized: Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        if (username == null || role == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized: Invalid or expired token");
            return;
        }
//
//        // Check if roles contain ROLE_ADMIN for admin endpoints
//        if (path.startsWith("/api/admin/") && (role == null || !role.contains("ROLE_ADMIN"))) {
//            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            httpResponse.getWriter().write("Forbidden: Admin access required");
//            return;
//        }
//
//        // Extract roles from the token
//        if (role == null || (!role.contains("ROLE_USER") && !role.contains("ROLE_ADMIN"))) {
//            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            httpResponse.getWriter().write("Forbidden: Insufficient role");
//            return;
//        }
        httpRequest.setAttribute("username", username);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
