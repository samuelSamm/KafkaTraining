package com.tecnosolution.KafkaTraining.security;

import com.tecnosolution.KafkaTraining.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // Inyectamos el filtro personalizado que procesará los tokens JWT
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitamos CSRF ya que las arquitecturas REST con JWT son Stateless y no usan Cookies
                .csrf(csrf -> csrf.disable())

                // 2. Configuración de reglas de autorización detalladas
                .authorizeHttpRequests(auth -> auth
                        // Endpoints totalmente públicos (Autenticación y herramientas de desarrollo)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // Restricciones específicas por método HTTP para la lógica de negocio
                        .requestMatchers(HttpMethod.GET, "/api/ventas/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/ventas/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/ventas/**").hasRole("ADMIN")

                        // Cualquier otra petición no especificada requerirá estar autenticado
                        .anyRequest().authenticated()
                )

                // 3. Modificamos la gestión de sesiones a STATELESS para que no guarde cookies en el servidor
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Posicionamos nuestro filtro JWT justo antes del filtro encargado del login tradicional
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // 5. Permitimos la visualización de los marcos (iframes) requeridos por la consola de H2
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    // Bean indispensable para procesar la autenticación inicial en el endpoint de login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Encriptador estándar para almacenar y verificar contraseñas de forma segura
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}