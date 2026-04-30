package com.in5bv.rourb.security;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}