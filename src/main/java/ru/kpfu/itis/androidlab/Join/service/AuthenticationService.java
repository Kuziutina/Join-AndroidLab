package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.androidlab.Join.details.CustomUserDetails;
import ru.kpfu.itis.androidlab.Join.service.interfaces.AuthenticationServiceInt;

@Service
public class AuthenticationService implements AuthenticationServiceInt {
    @Override
    public Long getUserIdByAuthentication(Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getDetails();

        return userDetails.getId();
    }
}
