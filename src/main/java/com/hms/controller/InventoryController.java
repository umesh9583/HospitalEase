package com.hms.controller;

import com.hms.entity.Medicine;
import com.hms.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private MedicineRepository medicineRepository;

    @GetMapping("/medicines-in-stock")
    public int getMedicinesInStock() {
        List<Medicine> medicines = medicineRepository.findAll();
        return medicines.stream().mapToInt(Medicine::getStock).sum();
    }
}