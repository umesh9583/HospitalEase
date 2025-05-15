package com.hms.repository;

import com.hms.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NurseRepository extends JpaRepository<Nurse, Long> {
    Optional<Nurse> findByMobile(String mobile);
}