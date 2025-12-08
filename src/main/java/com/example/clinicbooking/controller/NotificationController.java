package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Notification.NotificationResponse;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Quản lý thông báo")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/count-unread")
    public ResponseEntity<Integer> countNotification() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }

        Integer response = notificationService.countUnreadNotifications(cud.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotification() {
        // Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        List<NotificationResponse> response = notificationService.getNotificationsByUserId(currentUserId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<?> markNotificationAsRead(
            @PathVariable int notificationId,
            @RequestParam int userId) { // Cần xác thực người dùng sở hữu thông báo

        try {
            // Truyền ID thông báo và ID người dùng để đảm bảo tính bảo mật
            notificationService.markAsRead(notificationId, userId);

            return ResponseEntity.ok().body("Notification marked as read successfully.");

        } catch (IllegalArgumentException e) {
            // Xử lý lỗi nếu không tìm thấy thông báo hoặc người dùng không sở hữu
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }
}
