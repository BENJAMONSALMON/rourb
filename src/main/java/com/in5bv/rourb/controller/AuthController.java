package com.in5bv.rourb.controller;

import com.in5bv.rourb.entity.Users;
import com.in5bv.rourb.repository.UserRepository;
import com.in5bv.rourb.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Optional<Users> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            System.out.println("DEBUG: User not found: " + username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }

        Users user = userOpt.get();


        if (!passwordEncoder.matches(password, user.getEncryptedPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        String token = jwtTokenProvider.generateToken(authentication);

        String roleForFrontend = user.getRol().name();

        return ResponseEntity.ok(Map.of(
                "token",    token,
                "role",     roleForFrontend,
                "username", user.getUsername(),
                "userId", user.getIdUser()
        ));
    }
}