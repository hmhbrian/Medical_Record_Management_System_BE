package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.RoomResponse;
import com.example.ClinicBooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<List<RoomResponse>> getRoomsBySpecialty(@PathVariable int specialtyId) {
        return ResponseEntity.ok(roomService.getRoomsBySpecialty(specialtyId));
    }
}
