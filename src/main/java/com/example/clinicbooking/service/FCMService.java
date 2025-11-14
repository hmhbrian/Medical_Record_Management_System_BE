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
    public void sendAppointmentConfirmation(NotificationRequest request) throws FirebaseMessagingException {

        User user = userRepo.findById(request.getUserId()).orElse(null);
        if (user == null) {
            System.err.println("User not found for ID: " + request.getUserId() + ". Cannot send notification.");
            return;
        }

        //LƯU LỊCH SỬ TIN NHẮN (MESSAGES)
        Message savedMessage = saveNewMessage(request);

        // TRUY VẤN TOKEN
        List<FcmToken> tokens = fcmTokenRepo.findAllByUserAndIsActive(user,true);

        if (tokens.isEmpty()) {
            System.out.println("No active FCM tokens found for User: " + user.getId());
            // KHÔNG gửi thông báo, nhưng Message đã được lưu.
            return;
        }

        System.out.println("--- FCM Tokens found for User " + user.getId() + " ---");
        tokens.forEach(token -> {
            System.out.println("Token ID: " + token.getId() + ", Value: " + token.getToken().substring(0, 10) + "...");
        });
        System.out.println("-------------------------------------------------");

        List<String> tokenStrings = tokens.stream().map(FcmToken::getToken).toList();

        //XÂY DỰNG VÀ GỬI MULTICAST
        MulticastMessage message = buildMulticastMessage(request, tokenStrings);

        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);

        //LƯU LOG KẾT QUẢ VÀ TẠO ENTRY NOTIFICATION
        processFCMResponse(response, savedMessage, tokens, user);
    }

//    private Notifications createPendingNotification(User user, Message message) {
//        Notifications notif = new Notifications();
//        notif.setUser(user);
//        notif.setMessage(message);
//
//        // Ghi vào CSDL trước khi gửi lên Firebase
//        return notificationRepo.save(notif);
//    }

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

    private MulticastMessage buildMulticastMessage(NotificationRequest request, List<String> tokenStrings) {
        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();

        return MulticastMessage.builder()
                .setNotification(notification)
                .putAllData(request.getData())
                .addAllTokens(tokenStrings)
                .build();
    }

    private void processFCMResponse(BatchResponse response, Message message, List<FcmToken> targetTokens, User user) {
        LocalDateTime sentTime = LocalDateTime.now();
        // Danh sách token cần cập nhật trạng thái
        List<FcmToken> tokensToUpdate = new ArrayList<>();

        //Xử lý kết quả trả về từ FCM (Kết quả gửi tới từng token)
        for (int i = 0; i < response.getResponses().size(); i++) {
            SendResponse sendResponse = response.getResponses().get(i);
            FcmToken token = targetTokens.get(i);

            MessageLog log = new MessageLog();
            log.setMessage(message);
            log.setToken(token);
            log.setSendAt(sentTime);

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
                System.err.println("FCM Error for token " + token.getToken() + ": " + sendResponse.getException().getMessage());
            }
            messageLogRepo.save(log);
        }
        // Lưu các token bị đánh dấu không hoạt động sau khi vòng lặp kết thúc
        if (!tokensToUpdate.isEmpty()) {
            fcmTokenRepo.saveAll(tokensToUpdate);
        }
    }
}
