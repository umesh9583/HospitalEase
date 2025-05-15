package com.hms.repository;

import com.hms.entity.Accountant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountantRepository extends JpaRepository<Accountant, Long> {
    Optional<Accountant> findByMobile(String mobile);
}