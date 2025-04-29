package com.example.demo;
// "Servicio para manejar la lógica de negocio de usuarios. Incluye la lógica de registro, login y validación de contraseñas."

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;  // Repositorio que maneja las operaciones CRUD

    @Autowired
    private PasswordEncoder passwordEncoder;  // Codificador de contraseñas para encriptar y validar

    // Método para registrar un nuevo usuario
    public User register(User user) {
        // Verifica si el nombre de usuario ya está en uso
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        // Verifica si el correo electrónico ya está registrado
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        // Encriptar la contraseña antes de guardarla
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Guardar el usuario en la base de datos
        return userRepository.save(user);
    }

    // Método para buscar un usuario por nombre de usuario
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Método para verificar si las contraseñas coinciden
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
