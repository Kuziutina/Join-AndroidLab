package ru.kpfu.itis.androidlab.Join.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.kpfu.itis.androidlab.Join.details.AuthProvider;
import ru.kpfu.itis.androidlab.Join.details.UserDetailsServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static ru.kpfu.itis.androidlab.Join.security.SecurityConstants.SIGN_UP_URL;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private AuthProvider authenticationProvider;
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public WebSecurity(AuthProvider authenticationProvider,
                       RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.authenticationProvider = authenticationProvider;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);

        http.cors().and().csrf().disable()
                .authorizeRequests()
               .antMatchers("/login*").permitAll()
               .antMatchers("/login/*").permitAll()
               .antMatchers("/registration").permitAll()
               .antMatchers("/registration/*").permitAll()
               .antMatchers("/recovery").permitAll()
               .antMatchers("/recovery/*").permitAll()
               .anyRequest().authenticated()
                // .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .failureForwardUrl("/login?error")
                .usernameParameter("mail")
                .successHandler(successHandler())
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .authenticationProvider(authenticationProvider)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new NoRedirectStrategy());
        return successHandler;
    }

}
