package com.in5bv.rourb.controller;

import com.in5bv.rourb.entity.Users;
import com.in5bv.rourb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user";
    }

    @GetMapping("/action")
    public String processAction(@RequestParam("id") Integer id,
                                @RequestParam("action") String action,
                                RedirectAttributes redirectAttributes) {
        try {
            Users user = userService.getUserById(id);
            redirectAttributes.addFlashAttribute("userEdit", user);
            return "redirect:/users?action=" + action;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
            return "redirect:/users";
        }
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Users user, RedirectAttributes redirectAttributes) {
        try {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating user.");
        }
        return "redirect:/users";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Users user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUsers(user.getIdUser(), user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user.");
        }
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("idUser") Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUsers(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not delete user.");
        }
        return "redirect:/users";
    }
}