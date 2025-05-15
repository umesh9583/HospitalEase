package com.hms.repository;

import com.hms.entity.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {
    Optional<Pharmacist> findByMobile(String mobile);
}