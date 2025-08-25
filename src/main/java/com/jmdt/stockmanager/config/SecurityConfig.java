package com.jmdt.stockmanager.config;


import com.jmdt.stockmanager.security.JWTAuthenticationEntryPoint;
import com.jmdt.stockmanager.security.JWTAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JWTAuthenticationEntryPoint entryPoint;

    @Autowired
    private JWTAuthenticationFilter authenticationFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final String[] PUBLIC_URLs = {
            "/api/auth/login",
            "/api/user/register/**",
            "/api/onboarding/register"
    };

    public static final String[] RESTRICTED_URLs = {
            "/api/stock/**",
    };

    Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> {
            try {
                csrf.disable()
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers(PUBLIC_URLs).permitAll()
                                .requestMatchers(HttpMethod.GET).permitAll()
                                .requestMatchers(RESTRICTED_URLs).authenticated()
                                .anyRequest().authenticated())
                        .exceptionHandling(exception -> exception
                                .authenticationEntryPoint(entryPoint))
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        httpSecurity.authenticationProvider(this.daoAuthenticationProvider());
        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

         return httpSecurity.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }
}
