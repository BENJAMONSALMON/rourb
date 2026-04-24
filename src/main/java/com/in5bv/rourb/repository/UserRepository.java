package com.in5bv.rourb.repository;

import com.in5bv.rourb.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {
}
