package ru.kpfu.itis.androidlab.Join.details;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Component
public class AuthProvider implements AuthenticationProvider {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserDetailsServiceImpl userDetailsService;

    public AuthProvider(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login  = authentication.getName();
        String password = authentication.getCredentials().toString();
        String tokenDevice = ((CustomUserDetails)authentication.getDetails()).getTokenDevice();

        User userDataOptional = userRepository.findUserByEmail(login);
        if (userDataOptional != null) {
            if (passwordEncoder.matches(password, userDataOptional.getPassword())) {
                userDataOptional.setTokenDevice(tokenDevice);
                userRepository.save(userDataOptional);

//                if (!userDataOptional.isConfirmed()) {
//                    throw new BadCredentialsException("Confirmation is failed");
//                }

            } else {
                throw new BadCredentialsException("Invalid login or password");
            }

        } else {
            throw new BadCredentialsException("invalid login or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        Collection<? extends GrantedAuthority> grantedAuthority = userDetails.getAuthorities();
        return new UsernamePasswordAuthenticationToken(userDetails, password, grantedAuthority);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}

