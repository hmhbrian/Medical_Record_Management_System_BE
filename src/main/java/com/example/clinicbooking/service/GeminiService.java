package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Chat.*;
import com.example.clinicbooking.config.GeminiConfig;
import com.example.clinicbooking.service.Chat.ChatContextService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final WebClient geminiWebClient;
    private final GeminiConfig geminiConfig;
    private final ChatContextService chatContextService;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.model}")
    private String model;

    /**
     * Main method để xử lý chat request
     */
    public ChatResponse chat(ChatRequest request) {
        try {
            // 1. Xây dựng system prompt
            String systemPrompt = buildSystemPrompt();

            // 2. Lấy context từ database nếu có userId
            String userContext = "";
            if (request.getUserId() != null) {
                userContext = chatContextService.getUserContext(request.getUserId());
            }

            // 3. Tìm bác sĩ nếu message có đề cập đến triệu chứng
            List<DoctorSuggestion> suggestedDoctors = chatContextService.findDoctorsBySymptoms(request.getMessage());

            // 4. Xây dựng prompt hoàn chỉnh
            String fullPrompt = systemPrompt;
            if (!userContext.isEmpty()) {
                fullPrompt += "\n\nThông tin bệnh nhân:\n" + userContext;
            }
            if (!suggestedDoctors.isEmpty()) {
                fullPrompt += "\n\nCác bác sĩ phù hợp: " + formatDoctorList(suggestedDoctors);
            }
            fullPrompt += "\n\nCâu hỏi của bệnh nhân: " + request.getMessage();

            // 5. Gọi Gemini API
            String aiResponse = callGeminiAPI(fullPrompt, request.getConversationHistory());

            // 6. Tạo response
            ChatResponse response = new ChatResponse();
            response.setMessage(aiResponse);
            response.setTimestamp(LocalDateTime.now());
            response.setSuggestedDoctors(suggestedDoctors.isEmpty() ? null : suggestedDoctors);

            // Metadata
            Map<String, Object> metadata = new HashMap<>();
            if (!suggestedDoctors.isEmpty()) {
                metadata.put("hasDoctorSuggestions", true);
                metadata.put("suggestionCount", suggestedDoctors.size());
            }
            response.setMetadata(metadata.isEmpty() ? null : metadata);

            return response;

        } catch (Exception e) {
            System.err.println("Error in chat: " + e.getMessage());
            e.printStackTrace();

            // Fallback response
            ChatResponse errorResponse = new ChatResponse();
            errorResponse.setMessage("Xin lỗi, tôi gặp sự cố khi xử lý yêu cầu của bạn. Vui lòng thử lại sau.");
            errorResponse.setTimestamp(LocalDateTime.now());
            return errorResponse;
        }
    }

    /**
     * Xây dựng system prompt cho AI
     */
    private String buildSystemPrompt() {
        return """
                Bạn là trợ lý y tế AI của Bệnh viện Medic TP.HCM.

                QUAN TRỌNG - Trả lời NGẮN GỌN (tối đa 5-6 câu):
                1. Trấn an bệnh nhân về triệu chứng
                2. Gợi ý chuyên khoa phù hợp
                3. Nếu có danh sách bác sĩ: CHỈ nói "Chúng tôi có các bác sĩ chuyên khoa [tên khoa] sẵn sàng hỗ trợ bạn"
                   KHÔNG liệt kê chi tiết từng bác sĩ (thông tin đã có trong danh sách)
                4. Hướng dẫn đặt lịch qua app

                Nguyên tắc:
                - Lịch sự, thân thiện nhưng NGẮN GỌN
                - KHÔNG chẩn đoán cụ thể
                - Triệu chứng nguy hiểm: khuyến cáo gặp bác sĩ NGAY
                - Tránh thuật ngữ y học phức tạp
                """;
    }

    /**
     * Gọi Gemini API để lấy response từ AI
     */
    private String callGeminiAPI(String prompt, List<ChatMessage> conversationHistory) {
        try {
            // Xây dựng request body theo format của Gemini API
            ObjectNode requestBody = objectMapper.createObjectNode();

            ArrayNode contents = objectMapper.createArrayNode();

            // Thêm lịch sử hội thoại nếu có
            if (conversationHistory != null && !conversationHistory.isEmpty()) {
                for (ChatMessage msg : conversationHistory) {
                    ObjectNode messageNode = objectMapper.createObjectNode();
                    ArrayNode partsArray = objectMapper.createArrayNode();

                    ObjectNode partObj = objectMapper.createObjectNode();
                    partObj.put("text", msg.getContent());
                    partsArray.add(partObj);

                    messageNode.put("role", msg.getRole().equals("assistant") ? "model" : "user");
                    messageNode.set("parts", partsArray);
                    contents.add(messageNode);
                }
            }

            // Thêm prompt hiện tại
            ObjectNode currentMessage = objectMapper.createObjectNode();
            ArrayNode partsArray = objectMapper.createArrayNode();

            ObjectNode partObj = objectMapper.createObjectNode();
            partObj.put("text", prompt);
            partsArray.add(partObj);

            currentMessage.put("role", "user");
            currentMessage.set("parts", partsArray);
            contents.add(currentMessage);

            requestBody.set("contents", contents);

            // Gọi API - API key phải gửi qua header x-goog-api-key
            String endpoint = "/models/" + model + ":generateContent";

            // Debug logging
            System.out.println("=== Gemini API Request ===");
            System.out.println("Full URL: https://generativelanguage.googleapis.com/v1beta" + endpoint);
            System.out.println("API Key (first 20): "
                    + geminiConfig.getApiKey().substring(0, Math.min(20, geminiConfig.getApiKey().length())) + "...");
            System.out.println("Request Body: " + requestBody.toPrettyString());

            String responseBody = geminiWebClient.post()
                    .uri(endpoint)
                    .header("x-goog-api-key", geminiConfig.getApiKey())
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse response
            JsonNode responseJson = objectMapper.readTree(responseBody);
            String aiText = responseJson
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText();

            return aiText;

        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get response from Gemini AI", e);
        }
    }

    /**
     * Format danh sách bác sĩ thành string để AI đọc
     */
    private String formatDoctorList(List<DoctorSuggestion> doctors) {
        StringBuilder sb = new StringBuilder();
        for (DoctorSuggestion doctor : doctors) {
            sb.append(String.format("- Bác sĩ %s, Chuyên khoa %s, %d năm kinh nghiệm\n",
                    doctor.getDoctorName(),
                    doctor.getSpecialty(),
                    doctor.getExperienceYears()));
        }
        return sb.toString();
    }
}
