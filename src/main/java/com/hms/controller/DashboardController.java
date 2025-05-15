package com.hms.controller;

import com.hms.entity.Doctor;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if (userId == null || role == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        switch (role) {
            case "PATIENT":
                return ResponseEntity.ok(Map.of("redirect", "/patient-dashboard.html"));
            case "DOCTOR":
                Optional<Doctor> doctor = doctorRepository.findById(userId);
                if (!doctor.isPresent()) {
                    return ResponseEntity.notFound().build();
                }
                List<com.hms.entity.Appointment> todayAppointments = appointmentRepository.findAll().stream()
                        .filter(a -> a.getDoctor().getId().equals(userId))
                        .filter(a -> a.getAppointmentDate().toLocalDate().isEqual(LocalDate.now()))
                        .toList();
                Map<String, Object> doctorDashboard = new HashMap<>();
                doctorDashboard.put("doctor", doctor.get());
                doctorDashboard.put("todayAppointments", todayAppointments);
                return ResponseEntity.ok(doctorDashboard);
            case "ADMIN":
                Map<String, Object> adminDashboard = new HashMap<>();
                adminDashboard.put("totalPatients", patientRepository.count());
                adminDashboard.put("totalAppointments", appointmentRepository.count());
                return ResponseEntity.ok(adminDashboard);
            default:
                return ResponseEntity.ok(Map.of("redirect", "/index.html"));
        }
    }
}