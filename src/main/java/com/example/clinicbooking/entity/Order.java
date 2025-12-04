package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;


@Entity
@Immutable // Rất quan trọng: Đánh dấu là không thể chỉnh sửa
@Subselect("SELECT * FROM unified_service_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    //Sử dụng EmbeddedId thay cho Id đơn
    @EmbeddedId
    private OrderId id;
    @Column(name = "service_name")
    private String serviceName;

    private String result;
    @Column(name = "requested_date")
    private LocalDateTime requestedAt;
    @Column(name = "result_date")
    private LocalDateTime completedAt;
    @Enumerated(EnumType.STRING) // Dùng Enum cho trạng thái để dễ quản lý
    @Column(nullable = false)
    private ServiceStatus status;

    @ManyToOne
    @JoinColumn(name = "record_id")
    private MedicalRecord medicalRecord;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // Nhân viên thực hiện dịch vụ
    @Column(name = "staff_name")
    private String staffName;
    @Column(name = "staff_code")
    private String staffCode;
}
