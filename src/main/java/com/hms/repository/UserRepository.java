package com.hms.repository;

import com.hms.entity.Patient;
import com.hms.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndRole(String username, String role);
    boolean existsByUsername(String username);
	Optional<Patient> findByUsername(String username);
}
