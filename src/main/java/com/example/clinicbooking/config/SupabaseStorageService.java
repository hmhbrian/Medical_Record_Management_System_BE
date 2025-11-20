package com.example.clinicbooking.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
public class SupabaseStorageService {
    private final WebClient webClient;
    private final String storageUploadUrl;
    private final String publicBaseUrl;

    private static final String UPLOAD_ENDPOINT = "/storage/v1/object/";

    public SupabaseStorageService(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.service-key}") String serviceKey,
            @Value("${supabase.storage-bucket-name}") String bucketName,
            @Value("${supabase.storage-base-url}") String publicBaseUrl) {

        this.publicBaseUrl = publicBaseUrl;
        this.storageUploadUrl = supabaseUrl + UPLOAD_ENDPOINT + bucketName;

        this.webClient = WebClient.builder()
                .baseUrl(storageUploadUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + serviceKey)
                .defaultHeader("apikey", serviceKey)
                .build();
    }

    public String uploadFile(MultipartFile file, String folderPath, String uniqueFileName) {
        if (file.isEmpty()) {
            throw new RuntimeException("Tệp tải lên không được để trống.");
        }

        try {
            // Loại bỏ dấu '/' ở đầu/cuối folderPath nếu có
            if (folderPath.startsWith("/")) folderPath = folderPath.substring(1);
            if (!folderPath.endsWith("/")) folderPath = folderPath + "/";

            // Tên file đã được định dạng duy nhất sẽ nằm ở cuối path
            String storageFilePath = folderPath + uniqueFileName; // Lưu ý: file.getName() trong MultipartFile là tên tham số, không phải tên file gốc. Cần lấy tên file đã tạo duy nhất từ logic service gọi

            // Lấy byte array của file và kích thước
            byte[] fileBytes = file.getBytes();
            long contentLength = fileBytes.length; // Kích thước tệp tính bằng byte

            System.out.println("Content Type: " + file.getContentType());
            // Thực hiện POST/PUT (Supabase dùng PUT để tải lên)
            webClient.put()
                    .uri(uriBuilder -> uriBuilder.path("/{path}").queryParam("upsert", "true").build(storageFilePath))
                    .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                    .body(BodyInserters.fromResource(new ByteArrayResource(file.getBytes())))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Chặn chờ kết quả (phù hợp với service đồng bộ)

            // Trả về URL công khai
            return publicBaseUrl + "/" + storageFilePath;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc tệp để tải lên Supabase: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tải tệp lên Supabase Storage: " + e.getMessage(), e);
        }
    }
}
