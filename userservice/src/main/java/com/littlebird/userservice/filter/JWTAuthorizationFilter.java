package com.littlebird.userservice.filter;

import com.littlebird.userservice.contants.JWTErrorCode;
import com.littlebird.userservice.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private JWTUtils jwtUtils;

    public  JWTAuthorizationFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!header.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), JWTErrorCode.INVALID_JWT_TOKEN.getMessage());
            return;
        }

        String token = header.substring(7);

        if(!jwtUtils.validateToken(token)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), JWTErrorCode.INVALID_JWT_TOKEN.getMessage());
            return;
        }



        // Get user identity and set it on the spring security context
        UserDetails userDetails = jwtUtils.getUserDetails(token);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        List.of() : userDetails.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
