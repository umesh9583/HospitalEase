package com.hms.controller;

import com.hms.entity.User;
import com.hms.service.PatientService;
import com.hms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @PostMapping("/form-login")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        Map<String, Object> response = new HashMap<>();
        User user = userService.authenticate(username, password, role);

        if (user != null) {
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("userId", user.getId());
        } else {
            response.put("success", false);
            response.put("message", "Invalid username or password");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registerPatient")
    public ResponseEntity<Map<String, Object>> registerPatient(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String name = request.getParameter("name");
            String aadhar = request.getParameter("aadhar_number");
            String mobile = request.getParameter("mobile");
            String address = request.getParameter("address");
            int age = Integer.parseInt(request.getParameter("age"));
            String bloodGroup = request.getParameter("blood_group");
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            boolean registered = patientService.registerPatient(
                    name, aadhar, mobile, address, age, bloodGroup, username, password
            );

            if (registered) {
                response.put("success", true);
                response.put("message", "Registration successful");
            } else {
                response.put("success", false);
                response.put("message", "Registration failed. Username or Aadhar may already exist.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
