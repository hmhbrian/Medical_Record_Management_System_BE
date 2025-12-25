package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Notification.NotificationRequest;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.entity.Message;
import com.example.clinicbooking.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FCMService {
    private FcmTokenRepository fcmTokenRepo;
    private MessageRepository messagesRepo;
    private MessageLogRepository messageLogRepo;
    private NotificationRepository notificationRepo;
    private UserRepository userRepo;
    private ObjectMapper objectMapper;

    // GỬI THÔNG BÁO LỊCH HẸN
    @Transactional
    public void sendAppointmentConfirmation(NotificationRequest request) {

        User user = userRepo.findById(request.getUserId()).orElse(null);
        if (user == null) {
            System.err.println("User not found for ID: " + request.getUserId() + ". Cannot send notification.");
            return;
        }

        // 1. LƯU LỊCH SỬ TIN NHẮN (MESSAGES)
        Message savedMessage = saveNewMessage(request);

        // 2. TRUY VẤN TOKEN
        List<FcmToken> tokens = fcmTokenRepo.findAllByUserAndIsActive(user, true);

        if (tokens.isEmpty()) {
            System.out.println("No active FCM tokens found for User: " + user.getId());
            return;
        }

        System.out.println("--- FCM Tokens found for User " + user.getId() + " ---");
        tokens.forEach(token -> {
            System.out.println("Token ID: " + token.getId() + ", Value: " + token.getToken().substring(0, 10) + "...");
        });
        System.out.println("-------------------------------------------------");

        // 3. VÒNG LẶP GỬI ĐƠN LẺ TỪNG TOKEN VÀ XỬ LÝ PHẢN HỒI
        List<MessageLog> logsToSave = new ArrayList<>();
        List<FcmToken> tokensToDeactivate = new ArrayList<>();
        boolean isAnySendSuccessful = false;
        LocalDateTime sentTime = LocalDateTime.now();

        for (FcmToken token : tokens) {

            MessageLog log = new MessageLog();
            log.setMessage(savedMessage);
            log.setToken(token);
            log.setSentAt(sentTime);

            try {
                // XÂY DỰNG MESSAGE ĐƠN LẺ VÀ GỬI
                com.google.firebase.messaging.Message singleMessage = buildSingleMessage(request, token.getToken());

                FirebaseMessaging.getInstance().send(singleMessage);

                // Gửi thành công
                log.setStatus("SUCCESS");
                isAnySendSuccessful = true;

            } catch (FirebaseMessagingException e) {
                // Gửi thất bại
                String errorCode = (e.getMessagingErrorCode() != null) ? e.getMessagingErrorCode().name()
                        : "UNKNOWN_ERROR";
                log.setStatus("FAILED");
                log.setErrorMessage(errorCode + ": " + e.getMessage());

                // Xử lý lỗi token không hợp lệ
                if (MessagingErrorCode.UNREGISTERED.name().equals(errorCode)) {
                    System.err.println(
                            "Token " + token.getToken().substring(0, 10) + "... is UNREGISTERED. Marking as inactive.");
                    token.setActive(false);
                    tokensToDeactivate.add(token);
                }
                System.err.println(
                        "FCM Error for token " + token.getToken().substring(0, 10) + "... : " + e.getMessage());

            } finally {
                logsToSave.add(log);
            }
        }

        // 4. LƯU LOG, CẬP NHẬT TOKEN VÀ TẠO ENTRY NOTIFICATION
        if (!logsToSave.isEmpty()) {
            messageLogRepo.saveAll(logsToSave);
        }

        if (!tokensToDeactivate.isEmpty()) {
            fcmTokenRepo.saveAll(tokensToDeactivate);
        }

        // TẠO ENTRY NOTIFICATION (Nếu có ít nhất một lần gửi thành công)
        if (isAnySendSuccessful) {
            Notifications newNotification = new Notifications();
            newNotification.setUser(user);
            newNotification.setMessage(savedMessage);
            newNotification.setSentAt(sentTime);
            notificationRepo.save(newNotification);
        }
    }

    private Message saveNewMessage(NotificationRequest request) {
        Message msg = new Message();
        msg.setTitle(request.getTitle());
        msg.setBody(request.getBody());
        msg.setSendType("Token");
        msg.setSendBy(request.getSentBy());
        msg.setCreatedAt(LocalDateTime.now());

        try {
            // Lưu data payload dưới dạng JSON String
            String dataJson = objectMapper.writeValueAsString(request.getData());
            msg.setData(dataJson);
        } catch (JsonProcessingException e) {
            msg.setData("{}");
        }
        return messagesRepo.save(msg);
    }

    private com.google.firebase.messaging.Message buildSingleMessage(NotificationRequest request, String token) {
        // Sử dụng DATA-ONLY message để tránh FCM tự động hiển thị notification
        // Mobile app sẽ tự kiểm soát việc hiển thị qua Notifee
        return com.google.firebase.messaging.Message.builder()
                .putAllData(request.getData())
                .putData("title", request.getTitle()) // Thêm title vào data
                .putData("body", request.getBody()) // Thêm body vào data
                .setToken(token) // Chỉ định TOKEN CỤ THỂ
                .build();
    }

    private void processFCMResponse(BatchResponse response, Message message, List<FcmToken> targetTokens, User user) {
        LocalDateTime sentTime = LocalDateTime.now();
        // Danh sách token cần cập nhật trạng thái
        List<FcmToken> tokensToUpdate = new ArrayList<>();

        // Xử lý kết quả trả về từ FCM (Kết quả gửi tới từng token)
        for (int i = 0; i < response.getResponses().size(); i++) {
            SendResponse sendResponse = response.getResponses().get(i);
            FcmToken token = targetTokens.get(i);

            MessageLog log = new MessageLog();
            log.setMessage(message);
            log.setToken(token);
            log.setSentAt(sentTime);

            if (sendResponse.isSuccessful()) {
                log.setStatus("SUCCESS");

                // 5.2. TẠO ENTRY TRONG BẢNG NOTIFICATION (Chỉ khi gửi thành công)
                Notifications notif = new Notifications();
                notif.setUser(user);
                notif.setMessage(message);
                notif.setSentAt(sentTime);
                notificationRepo.save(notif);

            } else {
                String errorCode = sendResponse.getException().getMessagingErrorCode().name();
                log.setStatus("FAILED");
                log.setErrorMessage(errorCode + ": " + sendResponse.getException().getMessage());

                // Xử lý lỗi token không hợp lệ hoặc không đăng ký
                if ("TOKEN_NOT_REGISTERED".equals(errorCode)) {
                    System.err.println("Token " + token.getToken() + " is UNREGISTERED. Marking as inactive.");

                    // Đánh dấu là không hoạt động
                    token.setActive(false);
                    tokensToUpdate.add(token);
                }
                System.err.println(
                        "FCM Error for token " + token.getToken() + ": " + sendResponse.getException().getMessage());
            }
            messageLogRepo.save(log);
        }
        // Lưu các token bị đánh dấu không hoạt động sau khi vòng lặp kết thúc
        if (!tokensToUpdate.isEmpty()) {
            fcmTokenRepo.saveAll(tokensToUpdate);
        }
    }
}
