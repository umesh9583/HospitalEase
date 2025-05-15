package com.hms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hms.entity.Appointment;
import com.hms.service.AppointmentService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Map<String, Object>> cancelAppointment(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Appointment appointment = appointmentService.findById(id);
            if (appointment == null) {
                response.put("message", "Appointment not found");
                response.put("success", false);
                return ResponseEntity.ok(response);
            }
            appointment.setStatus("CANCELED");
            appointmentService.save(appointment);
            response.put("message", "Appointment canceled successfully");
            response.put("success", true);
        } catch (Exception e) {
            response.put("message", "Error canceling appointment");
            response.put("success", false);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> completeAppointment(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Appointment appointment = appointmentService.findById(id);
            if (appointment == null) {
                response.put("message", "Appointment not found");
                response.put("success", false);
                return ResponseEntity.ok(response);
            }
            appointment.setStatus("COMPLETED");
            appointmentService.save(appointment);
            response.put("message", "Appointment marked as completed");
            response.put("success", true);
        } catch (Exception e) {
            response.put("message", "Error completing appointment");
            response.put("success", false);
        }
        return ResponseEntity.ok(response);
    }
}