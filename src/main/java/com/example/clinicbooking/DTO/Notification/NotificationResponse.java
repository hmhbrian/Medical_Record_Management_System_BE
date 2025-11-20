package com.example.clinicbooking.DTO.Notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class NotificationResponse {
    private int id; // ID của bản ghi Notifications
    private boolean isRead;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentAt;

    // Từ Entity Message (Nội dung)
    private int messageId;
    private String title;
    private String body;

    // Payload Data (Sử dụng Map để dễ dàng xử lý JSON string đã lưu trong DB)
    private Map<String, String> data;
}
