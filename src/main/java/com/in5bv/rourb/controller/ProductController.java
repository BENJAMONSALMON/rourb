package com.in5bv.rourb.controller;

import com.in5bv.rourb.entity.Products;
import com.in5bv.rourb.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "product";
    }

    @GetMapping("/action")
    public String processAction(@RequestParam("id") Integer id,
                                @RequestParam("action") String action,
                                RedirectAttributes redirectAttributes) {
        try {
            Products product = productService.getProductById(id);
            redirectAttributes.addFlashAttribute("productEdit", product);
            return "redirect:/products?action=" + action;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found.");
            return "redirect:/products";
        }
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Products product, RedirectAttributes redirectAttributes) {
        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating product.");
        }
        return "redirect:/products";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Products product, RedirectAttributes redirectAttributes) {
        try {
            productService.updateProducts(product.getIdProduct(), null);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating product.");
        }
        return "redirect:/products";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("idProduct") Integer id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProducts(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not delete product.");
        }
        return "redirect:/products";
    }
}