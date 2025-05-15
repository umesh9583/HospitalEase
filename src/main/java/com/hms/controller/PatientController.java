package com.hms.controller;

import com.hms.entity.Patient;
import com.hms.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Get patient profile by user ID (used after login)
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getPatientProfile(@PathVariable Long userId) {
        Optional<Patient> patient = patientService.getPatientByUserId(userId);

        if (patient.isPresent()) {
            return ResponseEntity.ok(patient.get());
        } else {
            return ResponseEntity.status(404).body("Patient not found");
        }
    }

    // Update patient info
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        boolean updated = patientService.updatePatient(id, updatedPatient);
        if (updated) {
            return ResponseEntity.ok("Patient updated successfully");
        } else {
            return ResponseEntity.status(404).body("Patient not found");
        }
    }
}
