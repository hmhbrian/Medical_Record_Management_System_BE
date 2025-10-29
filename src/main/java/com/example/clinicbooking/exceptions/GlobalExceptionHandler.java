package com.example.clinicbooking.exceptions;

import com.example.clinicbooking.DTO.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Xử lý InvalidInputException, trả về HTTP Status 400 BAD REQUEST.
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidInputException(
            InvalidInputException ex, WebRequest request) {

        // Log lỗi chi tiết (trên BE)
        // logger.warn("Invalid Input Error: {}", ex.getMessage());

        // Trả về đối tượng ApiResponse với thông báo lỗi chi tiết (gửi về client)
        ApiResponse<?> errorResponse = new ApiResponse<>(
                false,
                ex.getMessage(), // <-- Sẽ là "Department not found"
                null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // Trả về 400
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleInternalAccessDenied(
            AccessDeniedException ex, WebRequest request) {

        // Đây là lỗi 403 (Forbidden) xảy ra bên trong code (Service/Controller) của bạn
        // Chứ không phải lỗi 403 bị chặn ở Filter Layer (đã được CustomAccessDeniedHandler xử lý)

        ApiResponse<?> errorResponse = new ApiResponse<>(
                false,
                ex.getMessage() != null ? ex.getMessage() : "Truy cập bị từ chối.", // Lấy thông báo "Unauthorized"
                null
        );

        // Trả về HTTP Status 403 Forbidden
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
