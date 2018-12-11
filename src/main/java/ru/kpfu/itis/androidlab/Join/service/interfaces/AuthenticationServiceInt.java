package ru.kpfu.itis.androidlab.Join.service.interfaces;

import org.springframework.security.core.Authentication;

public interface AuthenticationServiceInt {

    Long getUserIdByAuthentication(Authentication authentication);
}
