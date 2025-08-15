package com.example.ClinicBooking.Infrastructure.Repository;

import com.example.ClinicBooking.Domain.Entities.RoomTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypesRepository extends JpaRepository<RoomTypes, Integer> {
}
