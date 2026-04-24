package com.in5bv.rourb.service;

import com.in5bv.rourb.entity.Users;
import com.in5bv.rourb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImplement implements UserService{

    private final UserRepository userRepository;

    public UserServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users saveUser(Users user) {
        user.setIdUser(null);
        return userRepository.save(user);
    }

    @Override
    public Users getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public Users updateUsers(Integer idUpdate, Users user) {
        Users existing = userRepository.findById(idUpdate).orElseThrow(() -> new RuntimeException("User not found with: " + idUpdate));
        existing.setUserCode(user.getUserCode());
        existing.setEmail(user.getEmail());
        existing.setRol(user.getRol());
        existing.setState(user.getState());
        return userRepository.save(existing);

    }

    @Override
    public void deleteUsers(Integer idDelete) {
        Users existing = getUserById(idDelete);
        userRepository.delete(existing);
    }
}
