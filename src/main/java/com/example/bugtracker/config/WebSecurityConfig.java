package com.example.bugtracker.config;

import com.example.bugtracker.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
    private final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/updateProfile").authenticated()
                .requestMatchers("/profile").authenticated()
                .requestMatchers("/edit-profile").authenticated()
                .requestMatchers("/edit-profile-image").authenticated()
                .requestMatchers("/ticket/submit").authenticated()

                // DEVELOPER DASHBOARD ACCESS AUTHORITY
                .requestMatchers("/admin").hasAnyAuthority("ADMIN", "DEVELOPER", "PROJECT MANAGER")
                .requestMatchers("/admin/profile/**").hasAnyAuthority("ADMIN", "DEVELOPER", "PROJECT MANAGER")
                .requestMatchers("/admin/project/**").hasAnyAuthority("ADMIN", "PROJECT MANAGER")
                .requestMatchers("/admin/report/**").hasAnyAuthority("ADMIN", "PROJECT MANAGER")
                .requestMatchers("/admin/user/**").hasAuthority("ADMIN")
                .requestMatchers("/admin/bug/**").hasAnyAuthority("ADMIN", "DEVELOPER", "PROJECT MANAGER")
                .anyRequest().permitAll()
                .and()
                .formLogin().loginPage("/login")
                .usernameParameter("email").passwordParameter("password")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll().and()
                .csrf().csrfTokenRepository(new HttpSessionCsrfTokenRepository());

        return http.build();
    }


}
