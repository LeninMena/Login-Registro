package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Map;
import org.springframework.http.HttpStatus;

// "Controlador REST para manejar las rutas de registro y login de usuarios. Expone los endpoints de la API."
@RestController
@RequestMapping("/api/users")  // Ruta base para los endpoints de usuarios
@CrossOrigin(origins = "*")  // Permite el acceso desde cualquier origen
public class UserController {

    @Autowired
    private UserService userService;  // Servicio para manejar la lógica de usuarios

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.register(user);  // Registrar al usuario
            return ResponseEntity.ok(newUser);  // Retorna el usuario registrado
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));  // En caso de error, se envía el mensaje de error
        }
    }

    // Endpoint para el login de un usuario
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userService.findByUsername(user.getUsername());  // Buscar usuario por nombre de usuario
        if (existingUser.isPresent() && userService.checkPassword(user.getPassword(), existingUser.get().getPassword())) {  // Verificar la contraseña
            return ResponseEntity.ok(Map.of("message", "Login Successful!"));  // Si la autenticación es correcta
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid Username or Password."));  // Si no se autentica correctamente
        }
    }
}
