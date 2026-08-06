package com.sribalajipg.repository;

import com.sribalajipg.entity.Role;
import com.sribalajipg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobileNumber(String mobileNumber);
    boolean existsByRole(Role role);
}