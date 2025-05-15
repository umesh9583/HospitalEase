package com.hms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hms.entity.Appointment;
import com.hms.entity.Doctor;
import com.hms.service.AppointmentService;
import com.hms.service.DoctorService;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/appointments/{userId}")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable Long userId) {
        Doctor doctor = doctorService.findByUserId(userId);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        List<Appointment> appointments = appointmentService.findByDoctorId(doctor.getId());
        return ResponseEntity.ok(appointments);
    }
}