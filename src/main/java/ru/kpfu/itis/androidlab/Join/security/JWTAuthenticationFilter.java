package ru.kpfu.itis.androidlab.Join.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.kpfu.itis.androidlab.Join.details.CustomUserDetails;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static ru.kpfu.itis.androidlab.Join.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User creds = new ObjectMapper()
                                .readValue(request.getInputStream(), User.class);

            String tokenDevice = creds.getTokenDevice();

            CustomUserDetails userDetails = new CustomUserDetails(creds.getEmail(), creds.getPassword(), new ArrayList<>());
            userDetails.setTokenDevice(tokenDevice);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
            usernamePasswordAuthenticationToken.setDetails(userDetails);

            return authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.addHeader("error", failed.getLocalizedMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String token = JWT.create()
                            .withSubject(customUserDetails.getUsername())
                            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                            .sign(Algorithm.HMAC512(SECRET.getBytes()));


        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.getWriter().print("{\"user_id\": " + customUserDetails.getId() + ", \"" +HEADER_STRING + "\":\"" + TOKEN_PREFIX +  token + "\"}");
        response.getWriter().flush();
    }
}
