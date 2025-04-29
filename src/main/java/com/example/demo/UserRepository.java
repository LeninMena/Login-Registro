package com.example.demo;
//"Repositorio JPA para realizar operaciones CRUD sobre la entidad 'User' en la base de datos."

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
