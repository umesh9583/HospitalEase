package com.hms.service;

import com.hms.entity.*;
import com.hms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public abstract class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Autowired
    private AccountantRepository accountantRepository;

    /*@Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Try to find the user in each repository by mobile or aadharNumber
        Patient patient = patientRepository.findByMobile(identifier)
            .orElseGet(() -> patientRepository.findByAadharNumber(identifier)
                .orElse(null));
        if (patient != null) {
            return User.withUsername(identifier)
                .password(patient.getPassword())
                .roles(patient.getRole())
                .build();
        }

        Doctor doctor = doctorRepository.findByMobile(identifier)
            .orElse(null);
        if (doctor != null) {
            return User.withUsername(identifier)
                .password(doctor.getPassword())
                .roles(doctor.getRole())
                .build();
        }

        Nurse nurse = nurseRepository.findByMobile(identifier)
            .orElse(null);
        if (nurse != null) {
            return User.withUsername(identifier)
                .password(nurse.getPassword())
                .roles(nurse.getRole())
                .build();
        }

        Admin admin = adminRepository.findByMobile(identifier)
            .orElse(null);
        if (admin != null) {
            return User.withUsername(identifier)
                .password(admin.getPassword())
                .roles(admin.getRole())
                .build();
        }

        Pharmacist pharmacist = pharmacistRepository.findByMobile(identifier)
            .orElse(null);
        if (pharmacist != null) {
            return User.withUsername(identifier)
                .password(pharmacist.getPassword())
                .roles(pharmacist.getRole())
                .build();
        }

        Accountant accountant = accountantRepository.findByMobile(identifier)
            .orElse(null);
        if (accountant != null) {
            return User.withUsername(identifier)
                .password(accountant.getPassword())
                .roles(accountant.getRole())
                .build();
        }

        throw new UsernameNotFoundException("User not found with identifier: " + identifier);
    }*/
}