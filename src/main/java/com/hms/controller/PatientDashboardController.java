package com.hms.controller;

import com.hms.entity.Appointment;
import com.hms.entity.Bill;
import com.hms.entity.Patient;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.BillRepository;
import com.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patient/dashboard")
public class PatientDashboardController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BillRepository billRepository;

    @GetMapping("/profile/{patientId}")
    public ResponseEntity<Patient> getPatientProfile(@PathVariable Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        return patient.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/appointments/upcoming/{patientId}")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments(@PathVariable Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (!patient.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Appointment> upcomingAppointments = appointmentRepository.findAll().stream()
                .filter(a -> a.getPatient().getId().equals(patientId))
                .filter(a -> a.getAppointmentDate().isAfter(LocalDateTime.now()))
                .filter(a -> a.getStatus().equals("Scheduled"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(upcomingAppointments);
    }

    @GetMapping("/appointments/past/{patientId}")
    public ResponseEntity<List<Appointment>> getPastAppointments(@PathVariable Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (!patient.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Appointment> pastAppointments = appointmentRepository.findAll().stream()
                .filter(a -> a.getPatient().getId().equals(patientId))
                .filter(a -> a.getAppointmentDate().isBefore(LocalDateTime.now()) || a.getStatus().equals("Completed"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(pastAppointments);
    }

    @GetMapping("/bills/unpaid/{patientId}")
    public ResponseEntity<List<Bill>> getUnpaidBills(@PathVariable Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (!patient.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Bill> unpaidBills = billRepository.findAll().stream()
                .filter(b -> b.getPatient().getId().equals(patientId))
                .filter(b -> !b.getPaid())
                .collect(Collectors.toList());
        return ResponseEntity.ok(unpaidBills);
    }

    @GetMapping("/summary/{patientId}")
    public ResponseEntity<Map<String, Integer>> getDashboardSummary(@PathVariable Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (!patient.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Appointment> appointments = appointmentRepository.findAll().stream()
                .filter(a -> a.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());

        List<Bill> bills = billRepository.findAll().stream()
                .filter(b -> b.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());

        int upcomingAppointments = (int) appointments.stream()
                .filter(a -> a.getAppointmentDate().isAfter(LocalDateTime.now()))
                .filter(a -> a.getStatus().equals("Scheduled"))
                .count();

        int pastAppointments = (int) appointments.stream()
                .filter(a -> a.getAppointmentDate().isBefore(LocalDateTime.now()) || a.getStatus().equals("Completed"))
                .count();

        int unpaidBills = (int) bills.stream()
                .filter(b -> !b.getPaid())
                .count();

        Map<String, Integer> summary = new HashMap<>();
        summary.put("upcomingAppointments", upcomingAppointments);
        summary.put("pastAppointments", pastAppointments);
        summary.put("unpaidBills", unpaidBills);

        return ResponseEntity.ok(summary);
    }
}