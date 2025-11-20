package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Notification.NotificationResponse;
import com.example.clinicbooking.entity.Notifications;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.NotificationRepository;
import com.example.clinicbooking.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void markAsRead(int notificationId, int receiverId) {

        // Tìm thông báo theo ID
        Notifications notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + notificationId));

        // Xác thực quyền sở hữu
        // Đảm bảo ID người nhận (receiver_id) trong DB khớp với userId gửi lên
        if (notification.getUser().getId() != receiverId) {
            throw new IllegalArgumentException("User ID mismatch. Cannot update a notification you don't own.");
        }

        // 3. Cập nhật trạng thái nếu chưa đọc
        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepo.save(notification);
        }
    }
}
