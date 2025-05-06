package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Room;
import com.example.ClinicBooking.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface roomRepository extends JpaRepository<Room, Integer> {

}
