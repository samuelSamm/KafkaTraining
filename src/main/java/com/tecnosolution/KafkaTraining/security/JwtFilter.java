package com.tecnosolution.KafkaTraining.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // Inyectamos nuestra clase de utilidades JWT
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. Extraer el encabezado 'Authorization' de la petición HTTP
        final String authorizationHeader = request.getHeader("Authorization");
        //imprime en consola para que vaya viendo el camino que recorre
        System.out.println("Entra a la clase JwtFilter, al metodo doFilterInternal");
        System.out.println("Encabezado: "+authorizationHeader);
        String username = null;
        String jwt = null;

        // 2. El token JWT siempre debe viajar con el formato: "Bearer <token>"
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Cortamos los primeros 7 caracteres ("Bearer ")
            try {
                username = jwtUtil.extractUsername(jwt);
                System.out.println("Extraer el username del token: "+username);
            } catch (Exception e) {
                // NOTA: En producción, puedes registrar el log o manejar excepciones de tokens expirados/malformados
                logger.error("Error al extraer el username del token JWT: " + e.getMessage());
            }
        }

        // 3. Si encontramos un usuario y el contexto de seguridad de Spring aún no está autenticado...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Extraemos los roles directamente desde el cuerpo del JWT (sin ir a la Base de Datos)
            List<String> roles = jwtUtil.extractRoles(jwt);

            // Convertimos las cadenas de texto de los roles en objetos GrantedAuthority que Spring entiende
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // 4. Creamos el objeto de autenticación con el usuario, credenciales (null porque ya es JWT) y sus roles
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username, null, authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 5. Seteamos la autenticación en el Contexto Global de Spring Security
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 6. Pasamos el control al siguiente filtro en la cadena (o al Controller final)
        System.out.println("Al final realiza el filtro");
        filterChain.doFilter(request, response);
    }
}
