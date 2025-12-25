package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Auth.ChangePassDTO;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.repository.UserRepository;
import com.example.clinicbooking.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.example.clinicbooking.Utils.TextUtils.normalizeText;

@Tag(name = "Users", description = "Quản lý người dùng")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassDTO dto) {
        // 1. Get authenticated user ID from JWT token
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();
        System.out.println("--- User ID change: " + currentUserId + " ---");
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Vui lòng đăng nhập để thực hiện thao tác này"));
        }

        // 2. Retrieve user from database
        User user = userRepo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // 3. Validate old password
        String oldPasswordNormalized = normalizeText(dto.getOldPassword());
        if (!passwordEncoder.matches(oldPasswordNormalized, user.getPass())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Mật khẩu cũ không đúng"));
        }

        // Custom validation method
        boolean isPasswordMatching = isPasswordMatching(dto.getNewPassword(), dto.getConfirmPassword());

        // 4. Check if new password matches confirm password
        if (!isPasswordMatching) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Mật khẩu mới và xác nhận mật khẩu không khớp"));
        }

        // 5. Check if new password is different from old password
        String newPasswordNormalized = normalizeText(dto.getNewPassword());
        if (passwordEncoder.matches(newPasswordNormalized, user.getPass())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Mật khẩu mới phải khác mật khẩu cũ"));
        }

        // 6. Encode and save new password
        String encodedPassword = passwordEncoder.encode(newPasswordNormalized);
        user.setPass(encodedPassword);
        userRepo.save(user);

        // 7. Return success response
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Đổi mật khẩu thành công"));
    }

    // Custom validation method
    public boolean isPasswordMatching(String newPassword, String confirmPassword) {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}
