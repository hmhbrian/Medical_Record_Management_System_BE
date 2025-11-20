package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.FcmToken.FcmTokenRequest;
import com.example.clinicbooking.entity.FcmToken;
import com.example.clinicbooking.entity.Feedback;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.FcmTokenRepository;
import com.example.clinicbooking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepo;
    private final UserRepository userRepo;

    public ApiResponse<?> saveToken(FcmTokenRequest request) {
        User user = userRepo.findById(request.getUserId()).orElseThrow(() -> new InvalidInputException("User không tồn tại với id: " + request.getUserId()));

        //Tìm kiếm xem TOKEN NÀY đã được lưu cho USER NÀY chưa
        Optional<FcmToken> existingTokenOpt = fcmTokenRepo.findByUserAndToken(user, request.getFcmToken());

        if (existingTokenOpt.isPresent()) {
            //Token đã tồn tại và khớp với token mới gửi lên
            // => Không lưu, chỉ cần cập nhật lại updated_at hoặc đảm bảo active = true nếu cần
            FcmToken existingToken = existingTokenOpt.get();

            // đánh dấu là ACTIVE và cập nhật thời gian:
            if (!existingToken.isActive() || existingToken.getDeviceType() == null || !existingToken.getDeviceType().equals(request.getDeviceType())) {
                existingToken.setActive(true);
                existingToken.setUpdatedAt(LocalDateTime.now());
                // Cập nhật Device Type nếu thay đổi (hoặc bạn có thể bỏ qua)
                existingToken.setDeviceType(request.getDeviceType());
                fcmTokenRepo.save(existingToken);
                return new ApiResponse<>(true, "Cập nhật trạng thái FCM token thành công", null);
            }

            // Token đã tồn tại và active, không cần làm gì thêm
            return new ApiResponse<>(true, "FCM token đã tồn tại, không cần lưu lại", null);

        }else{
            FcmToken fcmToken = new FcmToken();
            fcmToken.setUser(user);
            fcmToken.setToken(request.getFcmToken());
            fcmToken.setUpdatedAt(LocalDateTime.now());
            fcmToken.setDeviceType(request.getDeviceType());
            fcmToken.setActive(true);
            fcmTokenRepo.save(fcmToken);

            return new ApiResponse<>(true, "Lưu FCM token thành công", null);
        }
    }
}
