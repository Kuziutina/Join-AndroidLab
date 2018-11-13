package ru.kpfu.itis.androidlab.Join.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
public final class RestAuthenticationEntryPoint
        implements AuthenticationEntryPoint {


    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException) throws IOException {

        response.setStatus(SC_UNAUTHORIZED);
        response.addHeader("error", authException.getMessage());
    }
}