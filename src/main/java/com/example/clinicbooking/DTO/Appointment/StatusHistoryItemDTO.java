package com.example.clinicbooking.DTO.Appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistoryItemDTO {
    private int statusId;
    private String statusName;
    private String reason;
    private LocalDateTime updateAt;
    private Integer updatedById;
    private String updatedByName;
    private String updatedByRole;
}
