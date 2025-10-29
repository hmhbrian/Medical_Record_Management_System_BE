package com.example.clinicbooking.exceptions;

import com.example.clinicbooking.DTO.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    // Sử dụng ObjectMapper để chuyển đổi đối tượng ApiResponse thành JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Thiết lập mã trạng thái và content type
        response.setStatus(HttpStatus.FORBIDDEN.value()); // 403
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Tạo đối tượng phản hồi tùy chỉnh
        ApiResponse<?> errorResponse = new ApiResponse<>(
                false,
                "Truy cập bị từ chối. Bạn không có quyền thực hiện hành động này.",
                null
        );

        // Viết JSON vào response body
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
