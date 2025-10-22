package com.example.hackaton2.auth;


import com.example.hackaton2.domain.Role;
import com.example.hackaton2.domain.User;
import com.example.hackaton2.jwt.JwtService;
import com.example.hackaton2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Registra un nuevo usuario con validaciones de rol, email y username únicos.
     * - Si el rol es BRANCH, el campo branch es obligatorio.
     * - Si el rol es CENTRAL, el campo branch debe quedar null.
     */
    public User register(User request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new IllegalArgumentException("El username ya existe.");
        if (userRepository.existsByEmail(request.getEmail()))
            throw new IllegalArgumentException("El email ya está registrado.");

        if (request.getRole() == Role.BRANCH && (request.getBranch() == null || request.getBranch().isEmpty()))
            throw new IllegalArgumentException("El campo 'branch' es obligatorio para usuarios BRANCH.");

        if (request.getRole() == Role.CENTRAL)
            request.setBranch(null);

        // Encriptar contraseña antes de guardar
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(request);
    }

    /**
     * Inicia sesión y devuelve un token JWT válido por 1 hora.
     */
    public String login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty())
            throw new IllegalArgumentException("Usuario no encontrado.");

        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new IllegalArgumentException("Contraseña incorrecta.");

        // Generar token JWT válido por 1 hora (3600 segundos)
        return jwtService.generate(user, 3600L);
    }
}