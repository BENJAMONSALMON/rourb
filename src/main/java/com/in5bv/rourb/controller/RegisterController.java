package com.in5bv.rourb.controller;

import com.in5bv.rourb.entity.Clients;
import com.in5bv.rourb.entity.Role;
import com.in5bv.rourb.entity.State;
import com.in5bv.rourb.entity.Users;
import com.in5bv.rourb.repository.ClientRepository;
import com.in5bv.rourb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showForm() {
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role,
            @RequestParam(required = false) Integer dpi
    ) {

        Role userRole = Role.valueOf(role.toUpperCase());

        Users user = new Users();
        user.setUsername(username);
        user.setEncryptedPassword(passwordEncoder.encode(password));
        user.setRol(userRole);

        Users savedUser = userRepository.save(user);

        if (userRole == Role.CLIENT) {
            Clients client = new Clients();
            client.setClientDpi(Long.valueOf(dpi));
            client.setClientName(username);
            client.setState(State.ACTIVE);

            clientRepository.save(client);
        }

        return ResponseEntity.ok(Map.of(
                "message", "Usuario registrado correctamente"
        ));
    }
}