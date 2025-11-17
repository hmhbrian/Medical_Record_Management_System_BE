package com.example.clinicbooking.DTO.ImagingTest;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImagingResultUploadRequest {
    private List<MultipartFile> files;

    // List các mô tả và cờ ảnh chính tương ứng với thứ tự files
    private List<String> descriptions;
    // Kết luận tổng hợp
    private String result;
    //Cờ xác nhận gửi kết quả
    private Boolean sendResult;
}
