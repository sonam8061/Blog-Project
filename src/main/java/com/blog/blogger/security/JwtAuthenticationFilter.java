package com.blog.blogger.security;


import com.blog.blogger.exception.BlogAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.util.StringUtils;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get JWT Token from http request
        String token = getJWTfromRequest(request);

        //validate token
        try {
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {

                //get username from token
                String username = tokenProvider.getUsernameFromJWT(token);

                //load user associated with token
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //set spring security

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (BlogAPIException e) {
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);
    }

    //Bearer<access Token>

    private String getJWTfromRequest(HttpServletRequest request){

        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }
}
