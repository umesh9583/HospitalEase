package com.hms.service.impl;

import com.hms.entity.Patient;
import com.hms.entity.User;
import com.hms.repository.PatientRepository;
import com.hms.repository.UserRepository;
import com.hms.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean registerPatient(String name, String aadhar, String mobile, String address, int age, String bloodGroup, String username, String password) {
        // Check if user or patient with same aadhar or username exists
        if (userRepository.findByUsername(username).isPresent() || patientRepository.findByAadharNumber(aadhar).isPresent()) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("PATIENT");

        user = userRepository.save(user);

        Patient patient = new Patient();
        patient.setName(name);
        patient.setAadharNumber(aadhar);
        patient.setMobile(mobile);
        patient.setAddress(address);
        patient.setAge(age);
        patient.setBloodGroup(bloodGroup);
        patient.setUser(user);

        patientRepository.save(patient);
        return true;
    }

    @Override
    public Optional<Patient> getPatientByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }

    @Override
    public boolean updatePatient(Long id, Patient updatedPatient) {
        Optional<Patient> existingOpt = patientRepository.findById(id);
        if (existingOpt.isPresent()) {
            Patient existing = existingOpt.get();
            existing.setName(updatedPatient.getName());
            existing.setAadharNumber(updatedPatient.getAadharNumber());
            existing.setMobile(updatedPatient.getMobile());
            existing.setAddress(updatedPatient.getAddress());
            existing.setAge(updatedPatient.getAge());
            existing.setBloodGroup(updatedPatient.getBloodGroup());
            patientRepository.save(existing);
            return true;
        }
        return false;
    }
}
