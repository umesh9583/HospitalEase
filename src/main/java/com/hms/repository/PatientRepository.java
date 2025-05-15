package com.hms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hms.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    @Query("SELECT p FROM Patient p WHERE p.user.username = ?1\r\n"+ "")
    Optional<Patient> findByUserUsername(String username);


	Optional<Patient> findByUserId(Long userId);

	Optional<Patient> findByAadharNumber(String aadhar);

}
