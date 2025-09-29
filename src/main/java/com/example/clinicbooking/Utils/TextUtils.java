package com.example.clinicbooking.Utils;

public class TextUtils {
    //chuẩn hóa chuỗi văn bản
    public static String normalizeText(String input) {
        if (input == null) {
            return null;
        }
        // trim() để bỏ trắng đầu/cuối
        // replaceAll("\\s+", " ") để thay nhiều khoảng trắng liên tiếp thành 1
        return input.trim().replaceAll("\\s+", " ");
    }
}
