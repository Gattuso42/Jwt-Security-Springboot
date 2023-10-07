package com.gattuso.jwtProject.Security.Filters;

import com.gattuso.jwtProject.Security.JwtUtils;
import com.gattuso.jwtProject.Service.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserDetailServiceImpl userDetailService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader!=null&&tokenHeader.startsWith("Bearer ")){
            String token = tokenHeader.substring(7);// Extract the token from Bearer
            if(jwtUtils.isTokenValid(token)){
                String username = jwtUtils.getUsernameFromToken(token);//Extract username from token
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username,null,userDetails.getAuthorities());//Get authorities
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);// Continue to the next filter

    }
}
