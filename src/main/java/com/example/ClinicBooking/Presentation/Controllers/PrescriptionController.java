package com.example.ClinicBooking.Presentation.Controllers;

import com.example.ClinicBooking.DTO.PrescriptionRequest;
import com.example.ClinicBooking.DTO.PrescriptionResponse;
import com.example.ClinicBooking.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    @Autowired
    private PrescriptionService service;

    @PostMapping
    public ResponseEntity<PrescriptionResponse> create(@RequestBody PrescriptionRequest request) {
        return ResponseEntity.ok(service.createPrescription(request));
    }

    @GetMapping("/by-record/{recordId}")
    public ResponseEntity<PrescriptionResponse> getByRecordId(@PathVariable Integer recordId) {
        return ResponseEntity.ok(service.getByRecordId(recordId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> update(@PathVariable Integer id,
                                               @RequestBody PrescriptionRequest request) {
        return ResponseEntity.ok(service.updatePrescription(id, request));
    }
}
