package com.tecnosolution.KafkaTraining.controller;

import com.tecnosolution.KafkaTraining.dto.LoginRequest;
import com.tecnosolution.KafkaTraining.dto.LoginResponse;
import com.tecnosolution.KafkaTraining.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        // 1. Autenticar usando el Manager de Spring Security. Si falla (clave errónea), lanza excepción automáticamente
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        // 2. Si la autenticación fue exitosa, extraemos el UserDetails principal
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Generamos el token JWT con los claims de los roles incluidos
        String token = jwtUtil.generateToken(userDetails);

        // 4. Retornamos el token envuelto en el objeto de respuesta
        return ResponseEntity.ok(new LoginResponse(token));
    }
}