package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Map;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint para registrar un nuevo usuario
    @Operation(summary = "Register a new user", description = "Registers a new user and returns the user data.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.register(user);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // Endpoint para hacer login y generar el token JWT
    @Operation(summary = "Login a user", description = "Logs in a user and returns a JWT token.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful, returns token"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userService.findByUsername(user.getUsername());
        
        // Verificar si el usuario existe y si las contraseñas coinciden
        if (existingUser.isPresent() && userService.checkPassword(user.getPassword(), existingUser.get().getPassword())) {
            // Generar el token JWT
            String token = userService.generateToken(user.getUsername());
            
            // Devolver la respuesta con el mensaje y el token
            return ResponseEntity.ok(Map.of("message", "Login Successful!", "token", token));  
        } else {
            // Si las credenciales son incorrectas, devolver un error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid Username or Password."));
        }
    }

    // Método GET: Obtener usuario por nombre de usuario
    @Operation(summary = "Get a user by username", description = "Returns the details of the user if found.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }
    }

    // Método DELETE: Eliminar usuario por nombre de usuario
    @Operation(summary = "Delete a user by username", description = "Deletes the user from the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            userService.deleteUser(username);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }
    }
}
