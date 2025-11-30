package com.example.clinicbooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class OrderId implements Serializable {
    // ID gốc (có thể trùng giữa 2 bảng)
    @Column(name = "order_id")
    private Integer orderId;

    // Loại dịch vụ (dùng để phân biệt ID gốc)
    @Column(name = "service_type")
    private String serviceType;
}
