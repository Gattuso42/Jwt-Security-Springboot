package com.gattuso.jwtProject.Security.Filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gattuso.jwtProject.Model.UserEntity;
import com.gattuso.jwtProject.Security.JwtUtils;
import com.gattuso.jwtProject.Service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.ScatteringByteChannel;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtUtils jwtUtils;
    private RefreshTokenService refreshTokenService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        UserEntity userEntity = null;
        String username = "";
        String password = "";
        try {
            userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
            username = userEntity.getUsername();
            password = userEntity.getPassword();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String token = jwtUtils.generateAccessToken(user.getUsername());
        response.addHeader("Authorization",token);
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername()).getRefreshToken();

        Map<String,Object>httpResponse = new HashMap<>();
        httpResponse.put("token",token);
        httpResponse.put("refreshToken",refreshToken);
        httpResponse.put("Message","Successful Authentication");
        httpResponse.put("Username",user.getUsername());

        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
