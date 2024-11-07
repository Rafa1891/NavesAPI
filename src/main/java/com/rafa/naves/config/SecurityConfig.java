package com.rafa.naves.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Desactiva CSRF para simplificar en desarrollo (deberías habilitarlo en producción)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/naves/**").authenticated() // Requiere autenticación para rutas de API
                .anyRequest().permitAll() // Permite cualquier otra ruta sin autenticación
            )
            .httpBasic(); // Usa autenticación básica (username y password)

        return http.build();
    }
}
