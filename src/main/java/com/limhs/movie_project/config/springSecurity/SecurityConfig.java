package com.limhs.movie_project.config.springSecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(csrf -> csrf.disable()) // CSRF 비활성화, CsrfFilter 설정 메서드
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**","/*.ico", "/login/**","/signup/**","/error","/","/home/**")
                        .permitAll()
                        .anyRequest().authenticated()
                ) .formLogin(login -> login
                        .loginPage("/login?message=loginFirst")
                        .loginProcessingUrl("/login")
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true"))

                .logout(logout -> logout //LogoutFilter 설정 메서드
                        .logoutUrl("/logout")       // POST 요청으로 로그아웃 처리
                        .logoutSuccessUrl("/")      // 로그아웃 후 이동
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .rememberMe(remember -> remember
                        .key("secureKey") // 쿠키 변조 방지용 서버 키
                        .userDetailsService(customUserDetailService) // 커스터마이징 UserDetailsService
                        .tokenValiditySeconds(7 * 24 * 60 * 60) // 1주일 유지
                );
        return  http.build();

    }
}