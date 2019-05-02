package ru.kpfu.itis.androidlab.Join.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.kpfu.itis.androidlab.Join.details.CustomUserDetails;
import ru.kpfu.itis.androidlab.Join.model.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static ru.kpfu.itis.androidlab.Join.security.SecurityConstants.HEADER_STRING;
import static ru.kpfu.itis.androidlab.Join.security.SecurityConstants.SECRET;
import static ru.kpfu.itis.androidlab.Join.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                                .build()
                                .verify(token.replace(TOKEN_PREFIX, ""))
                                .getSubject();

            if (user != null) {
                String data[] = user.split(",");
                User customUserDetails = new User();
                customUserDetails.setEmail(data[0]);
                if (!data[1].equals("null"))
                    customUserDetails.setId(Long.parseLong(data[1]));

                return new UsernamePasswordAuthenticationToken(customUserDetails, null, new ArrayList<>());
            }

            return null;
        }

        return null;
    }

}
