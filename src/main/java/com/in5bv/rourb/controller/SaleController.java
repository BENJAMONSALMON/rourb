package com.in5bv.rourb.controller;

import com.in5bv.rourb.entity.Sales;
import com.in5bv.rourb.service.SaleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("sales", saleService.getAllSales());
        return "sale";
    }

    @GetMapping("/action")
    public String processAction(@RequestParam("id") Integer id,
                                @RequestParam("action") String action,
                                RedirectAttributes redirectAttributes) {
        try {
            Sales sale = saleService.getSaleById(id);
            redirectAttributes.addFlashAttribute("saleEdit", sale);
            return "redirect:/sales?action=" + action;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sale not found.");
            return "redirect:/sales";
        }
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Sales sale, RedirectAttributes redirectAttributes) {
        try {
            saleService.saveSales(sale);
            redirectAttributes.addFlashAttribute("successMessage", "Sale registered successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating sale.");
        }
        return "redirect:/sales";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("idSale") Integer id, RedirectAttributes redirectAttributes) {
        try {
            saleService.deleteSales(id);
            redirectAttributes.addFlashAttribute("successMessage", "Sale deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not delete sale.");
        }
        return "redirect:/sales";
    }
}