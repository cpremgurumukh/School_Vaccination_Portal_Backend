package com.prem.vaccinationportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prem.vaccinationportal.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
