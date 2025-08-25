package com.example.productionservices.filters;

import com.example.productionservices.entities.User;
import com.example.productionservices.services.JwtService;
import com.example.productionservices.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{

        final String requestTokenHeader=request.getHeader("Authorization");
       if((requestTokenHeader ==null) || !requestTokenHeader.startsWith("Bearer")){
           filterChain.doFilter(request,response);
           return;
       }
        String token = requestTokenHeader.substring(7).trim();

       Long id=jwtService.getUserIdFromJwtToken(token);
       if(id!=null && SecurityContextHolder.getContext().getAuthentication()==null){
        User user=userService.getUserById(id);
UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

       }
       filterChain.doFilter(request,response);
        }
        catch (Exception e){
            handlerExceptionResolver.resolveException(request,response,null,e);
        }

//       with the response
    }
}
