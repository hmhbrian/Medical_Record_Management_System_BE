package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Chat.ChatRequest;
import com.example.clinicbooking.DTO.Chat.ChatResponse;
import com.example.clinicbooking.service.GeminiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "AI Chat với Gemini - Tư vấn y tế và gợi ý bác sĩ")
@RequiredArgsConstructor
public class ChatController {

    private final GeminiService geminiService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> chat(@Valid @RequestBody ChatRequest request) {
        // Validate
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Tin nhắn không được để trống", null));
        }

        // Process chat
        ChatResponse response = geminiService.chat(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Chat thành công", response));
    }
}
