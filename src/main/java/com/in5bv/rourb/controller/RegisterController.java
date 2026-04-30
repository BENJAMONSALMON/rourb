package com.in5bv.rourb.controller;

import com.in5bv.rourb.entity.Role;
import com.in5bv.rourb.entity.Users;
import com.in5bv.rourb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role) {

        if (usersRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error=userExists";
        }

        Users newUser = new Users();
        newUser.setUsername(username);
        newUser.setEncryptedPassword(passwordEncoder.encode(password));
        newUser.setRol(Role.valueOf(role));
        newUser.setState(true);

        usersRepository.save(newUser);

        System.out.println("DEBUG: Succesfull register, redirecting to /login");
        return "redirect:/login";
    }
}