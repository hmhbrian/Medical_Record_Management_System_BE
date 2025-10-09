package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Bed.BedRequest;
import com.example.clinicbooking.DTO.Bed.BedResponse;
import com.example.clinicbooking.DTO.Bed.OverViewResponse;
import com.example.clinicbooking.service.BedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/beds")
@RequiredArgsConstructor
@Tag(name = "Beds", description = "Quản lý giường bệnh")
public class BedController {

    private final BedService bedService;

    @GetMapping
    public ResponseEntity<Page<BedResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,        // 0/1/2/3
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(defaultValue = "1") Integer page,        // 1-based
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                bedService.listBeds(keyword, status, departmentId, page, size)
        );
    }

    @PostMapping
    public ResponseEntity<BedResponse> create(@RequestBody BedRequest req) {
        return ResponseEntity.ok(bedService.createBed(req));
    }

    @GetMapping("/overview")
    public ResponseEntity<OverViewResponse> overview() {
        return ResponseEntity.ok(bedService.overview());
    }
}
