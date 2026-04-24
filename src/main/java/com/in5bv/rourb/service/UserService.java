package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.Users;

import java.util.List;

public interface UserService {
    List<Users> getAllUsers();
    Users saveUser(Users user);
    Users getUserById(Integer id);
    Users updateUsers(Integer id, Users user);
    void deleteUsers(Integer id);
}
