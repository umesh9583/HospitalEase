package com.hms.controller;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;

    @Controller
    public class WebController {

        @GetMapping("/index")
        public String home() {
            return "redirect:/index.html";        
        }

        @GetMapping("/register")
        public String register() {
            return "redirect:/register.html";
        }

        @GetMapping("/schedule")
        public String schedule() {
            return "redirect:/schedule.html";
        }

        @GetMapping("/assign-doctor")
        public String assignDoctor() {
            return "redirect:/assign-doctor.html";
        }

        @GetMapping("/doctor-schedule")
        public String doctorSchedule() {
            return "redirect:/doctor-schedule.html";
        }
    }