package com.bom.shop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //추가
//    @Bean
//    public CsrfTokenRepository csrfTokenRepository() {
//        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//        repository.setHeaderName("X-XSRF-TOKEN");
//        return repository;
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable CSRF protection (개발 중에는 잠시 끄는게 좋음)
        http.csrf((csrf) -> csrf.disable());
//        http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository())
//                .ignoringRequestMatchers("/login")
//        );
        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/**").permitAll()
        );
        http.formLogin((formLogin) -> formLogin.loginPage("/login")
                .defaultSuccessUrl("/list")
//                .failureUrl("/")
        );

        http.logout(logout -> logout.logoutUrl("/logout"));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
