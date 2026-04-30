package com.in5bv.rourb.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Users")
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "user_code")
    private Integer userCode;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "encrypted_password")
    private String encryptedPassword;

    @Column(name = "email")
    private String email;

    @Column(name = "rol")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "state")
    private Boolean state;

    //detalles de usuario o user
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return encryptedPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return state != null && state;
    }

    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }

    public Integer getUserCode() { return userCode; }
    public void setUserCode(Integer userCode) { this.userCode = userCode; }

    public void setUsername(String username) { this.username = username; }

    public String getEncryptedPassword() { return encryptedPassword; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRol() { return role; }
    public void setRol(Role role) { this.role = role; }

    public Boolean getState() { return state; }
    public void setState(Boolean state) { this.state = state; }
}
