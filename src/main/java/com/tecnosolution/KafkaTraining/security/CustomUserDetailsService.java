package com.tecnosolution.KafkaTraining.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Simulamos dos usuarios del sistema encriptando su contraseña sobre la marcha con BCrypt
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .roles("ADMIN")
                    .build();
        } else if ("usuario".equals(username)) {
            return User.builder()
                    .username("usuario")
                    .password(passwordEncoder.encode("password123"))
                    .roles("USER")
                    .build();
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}