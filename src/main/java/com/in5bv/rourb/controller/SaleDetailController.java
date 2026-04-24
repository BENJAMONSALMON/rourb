package com.in5bv.rourb.controller;

import com.in5bv.rourb.entity.SaleDetails;
import com.in5bv.rourb.service.SaleDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sale-details")
public class SaleDetailController {

    private final SaleDetailService saleDetailService;

    public SaleDetailController(SaleDetailService saleDetailService) {
        this.saleDetailService = saleDetailService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("saleDetails", saleDetailService.getAllSaleDetails());
        return "sale-detail"; // Asegúrate de que tu HTML se llame así
    }

    @GetMapping("/action")
    public String processAction(@RequestParam("id") Integer id,
                                @RequestParam("action") String action,
                                RedirectAttributes redirectAttributes) {
        try {
            SaleDetails detail = saleDetailService.getSaleDetailById(id);
            redirectAttributes.addFlashAttribute("detailEdit", detail);
            return "redirect:/sale-details?action=" + action;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sale detail not found.");
            return "redirect:/sale-details";
        }
    }

    @PostMapping("/save")
    public String save(@ModelAttribute SaleDetails saleDetail, RedirectAttributes redirectAttributes) {
        try {
            saleDetailService.saveSaleDetails(saleDetail);
            redirectAttributes.addFlashAttribute("successMessage", "Detail added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding sale detail.");
        }
        return "redirect:/sale-details";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute SaleDetails saleDetail, RedirectAttributes redirectAttributes) {
        try {
            saleDetailService.updateSaleDetails(saleDetail.getIdSaleDetail(), saleDetail);
            redirectAttributes.addFlashAttribute("successMessage", "Detail updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating sale detail.");
        }
        return "redirect:/sale-details";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("idSaleDetail") Integer id, RedirectAttributes redirectAttributes) {
        try {
            saleDetailService.deleteSaleDetails(id);
            redirectAttributes.addFlashAttribute("successMessage", "Detail deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not delete detail.");
        }
        return "redirect:/sale-details";
    }
}