package com.example.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import io.jsonwebtoken.security.Keys;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")  // Se obtiene del application.properties
    private String jwtSecretString;

    private Key jwtSecret;

    @PostConstruct
    public void init() {
        // Inicialización de la clave secreta con la longitud adecuada para HS256
        this.jwtSecret = Keys.hmacShaKeyFor(jwtSecretString.getBytes());
    }

    // Método para registrar un usuario
    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    // Método para encontrar un usuario por nombre de usuario
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Método para verificar si las contraseñas coinciden
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // Método para generar el token JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // El token expira en 1 día
                .signWith(jwtSecret, SignatureAlgorithm.HS256) // Usar la clave secreta con el algoritmo HS256
                .compact();
    }

    // Método para eliminar un usuario por nombre de usuario
    public void deleteUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new RuntimeException("Usuario no encontrado.");
        }
    }
}
