package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Notification.NotificationResponse;
import com.example.clinicbooking.entity.Notifications;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.NotificationRepository;
import com.example.clinicbooking.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepo;
    private final UserRepository userRepo;
    private final ObjectMapper objectMapper;

    // Đếm số lượng Notifications chưa đọc cho một userId cụ thể
    public Integer countUnreadNotifications(int userId) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new InvalidInputException("User not found!")
        );

        return notificationRepo.countByUserAndIsReadFalse(user);
    }

    // Lấy tất cả Notifications cho một userId cụ thể
    public List<NotificationResponse> getNotificationsByUserId(int userId) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new InvalidInputException("User not found!")
        );

        // Truy vấn tất cả Notifications cho userId
        List<Notifications> entities = notificationRepo.findAllByUser(user);

        // Ánh xạ thủ công và xử lý JSON Data/Múi giờ
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Hàm ánh xạ thủ công từ Notifications Entity sang DTO, bao gồm xử lý JSON và Múi giờ.
     */
    private NotificationResponse convertToDto(Notifications entity) {

        // 1. Ánh xạ các trường cơ bản từ Notifications và Message
        NotificationResponse dto = NotificationResponse.builder()
                .id(entity.getId())
                .isRead(entity.isRead())
                .messageId(entity.getMessage().getId())
                .title(entity.getMessage().getTitle())
                .body(entity.getMessage().getBody())
                .sentAt(entity.getSentAt())
                .build();

        // 3. Xử lý JSON String trong trường Data
        try {
            // Chuyển chuỗi JSON data từ Message Entity sang Map
            Map<String, String> dataMap = objectMapper.readValue(
                    entity.getMessage().getData(),
                    new TypeReference<Map<String, String>>() {}
            );
            dto.setData(dataMap);

        } catch (Exception e) {
            System.err.println("Error parsing data JSON for message ID: " + entity.getMessage().getId() + ". Setting data to empty map.");
            dto.setData(Map.of()); // Trả về Map rỗng nếu lỗi
        }

        return dto;
    }
}
