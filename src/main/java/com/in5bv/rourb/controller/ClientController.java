package com.in5bv.rourb.controller;

import com.in5bv.rourb.entity.Clients;
import com.in5bv.rourb.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "client"; // Nombre de la vista HTML
    }

    @GetMapping("/action")
    public String processAction(@RequestParam("id") Integer id,
                                @RequestParam("action") String action,
                                RedirectAttributes redirectAttributes) {
        try {
            Clients client = clientService.getClientById(id);
            redirectAttributes.addFlashAttribute("clientEdit", client);
            return "redirect:/clients?action=" + action;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Client not found.");
            return "redirect:/clients";
        }
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Clients client, RedirectAttributes redirectAttributes) {
        try {
            clientService.saveClient(client);
            redirectAttributes.addFlashAttribute("successMessage", "Client created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating client.");
        }
        return "redirect:/clients";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Clients client, RedirectAttributes redirectAttributes) {
        try {
            clientService.updateClients(client.getIdClient(), client);
            redirectAttributes.addFlashAttribute("successMessage", "Client updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating client.");
        }
        return "redirect:/clients";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("idClient") Integer id, RedirectAttributes redirectAttributes) {
        try {
            clientService.deleteClients(id);
            redirectAttributes.addFlashAttribute("successMessage", "Client deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not delete client.");
        }
        return "redirect:/clients";
    }
}