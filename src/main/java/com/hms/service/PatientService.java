package com.hms.service;

import com.hms.entity.Patient;

import java.util.Optional;

public interface PatientService {
    boolean registerPatient(String name, String aadhar, String mobile, String address, int age, String bloodGroup, String username, String password);
    Optional<Patient> getPatientByUserId(Long userId);
    boolean updatePatient(Long id, Patient updatedPatient);
}
