package com.in5bv.rourb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home-client")
    public String homeClient() {
        return "home-client";
    }

    @GetMapping("/home-seller")
    public String homeSeller() {
        return "home-seller";
    }
}