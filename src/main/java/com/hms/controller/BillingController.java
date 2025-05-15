package com.hms.controller;

import com.hms.entity.Bill;
import com.hms.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
public class BillingController {

    @Autowired
    private BillRepository billRepository;

    // Create a new bill
    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill savedBill = billRepository.save(bill);
        return ResponseEntity.ok(savedBill);
    }

    // Get all bills
    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        return ResponseEntity.ok(bills);
    }

    // Get a bill by ID
    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        Optional<Bill> bill = billRepository.findById(id);
        return bill.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a bill (e.g., mark as paid)
    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long id, @RequestBody Bill updatedBill) {
        Optional<Bill> existingBill = billRepository.findById(id);
        if (existingBill.isPresent()) {
            Bill bill = existingBill.get();
            bill.setPatient(updatedBill.getPatient());
            bill.setAmount(updatedBill.getAmount());
            bill.setPaid(updatedBill.getPaid());
            Bill savedBill = billRepository.save(bill);
            return ResponseEntity.ok(savedBill);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a bill
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            billRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}