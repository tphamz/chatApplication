package com.example.api_server.filter;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.api_server.service.AuthService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.api_server.model.User;

@Component
public class AuthFilter extends OncePerRequestFilter{
    @Autowired 
    AuthService tokenService;
    
    Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        try{
            String JWTToken = this.getTokenFromHeader(request);
            if(JWTToken!=null){
                User user = tokenService.authenticate(JWTToken);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null /** replace with entitty later*/, new ArrayList<>()/** replace with policy later*/);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }catch(JWTVerificationException e){
            logger.error("JWTVerificationException::" + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Access Token");
        }catch(Exception e){
            logger.error("Exception::" + e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    private String getTokenFromHeader(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader==null) return null;
        return authHeader.replace("Bearer ", ""); 
    }
}
