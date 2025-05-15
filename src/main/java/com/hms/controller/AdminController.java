package com.hms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hms.entity.Appointment;
import com.hms.entity.Doctor;
import com.hms.service.AppointmentService;
import com.hms.service.DoctorService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/appointments/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> assignDoctor(
            @RequestParam Long appointmentId,
            @RequestParam Long doctorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Appointment appointment = appointmentService.findById(appointmentId);
            Doctor doctor = doctorService.findById(doctorId);
            if (appointment == null || doctor == null) {
                response.put("message", "Appointment or Doctor not found");
                response.put("success", false);
                return ResponseEntity.ok(response);
            }
            appointment.setDoctor(doctor);
            appointmentService.save(appointment);
            response.put("message", "Doctor assigned successfully");
            response.put("success", true);
        } catch (Exception e) {
            response.put("message", "Error assigning doctor");
            response.put("success", false);
        }
        return ResponseEntity.ok(response);
    }
}