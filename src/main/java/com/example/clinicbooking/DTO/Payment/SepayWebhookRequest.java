package com.example.clinicbooking.DTO.Payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO nhận webhook callback từ SePay khi có giao dịch chuyển khoản
 * 
 * QUAN TRỌNG: Field names này là THẬT từ SePay webhook (đã verify 10/12/2025)
 * 
 * Ví dụ JSON THẬT từ SePay:
 * {
 * "gateway": "MBBank",
 * "transactionDate": "2025-12-10 18:08:00",
 * "accountNumber": "0337013824",
 * "content": "PAY11- Ma GD ACSP/ 8H081982",
 * "transferType": "in",
 * "transferAmount": 5000,
 * "referenceCode": "FT25344203196434",
 * "id": 34842698
 * }
 */
@Data
public class SepayWebhookRequest {

    /**
     * ID giao dịch từ SePay
     * Ví dụ: 34842698
     */
    @JsonProperty("id")
    private Long id;

    /**
     * Tên cổng thanh toán (ngân hàng)
     * Ví dụ: "MBBank", "Vietinbank", "Techcombank"
     */
    @JsonProperty("gateway")
    private String gateway;

    /**
     * Thời gian giao dịch
     * Format: "yyyy-MM-dd HH:mm:ss"
     * Ví dụ: "2025-12-10 18:08:00"
     */
    @JsonProperty("transactionDate")
    private String transactionDate;

    /**
     * Số tài khoản nhận tiền (của bạn)
     * Ví dụ: "0337013824"
     */
    @JsonProperty("accountNumber")
    private String accountNumber;

    /**
     * Nội dung chuyển khoản (QUAN TRỌNG - chứa paymentCode)
     * Ví dụ: "PAY11- Ma GD ACSP/ 8H081982"
     * 
     * Cần parse để lấy "PAY11"
     */
    @JsonProperty("content")
    private String content;

    /**
     * Loại giao dịch: "in" (tiền vào) hoặc "out" (tiền ra)
     * Lưu ý: lowercase "in", không phải "IN"
     */
    @JsonProperty("transferType")
    private String transferType;

    /**
     * Mô tả giao dịch
     * Ví dụ: "BankAPINotify PAY11- Ma GD ACSP/ 8H081982"
     */
    @JsonProperty("description")
    private String description;

    /**
     * Số tiền chuyển khoản (VND)
     * Ví dụ: 5000
     */
    @JsonProperty("transferAmount")
    private Long transferAmount;

    /**
     * Mã tham chiếu giao dịch từ ngân hàng
     * Ví dụ: "FT25344203196434"
     */
    @JsonProperty("referenceCode")
    private String referenceCode;

    /**
     * Số dư tích lũy sau giao dịch
     * Ví dụ: 35000
     */
    @JsonProperty("accumulated")
    private Long accumulated;

    /**
     * Sub-account (thường null)
     */
    @JsonProperty("subAccount")
    private String subAccount;

    /**
     * Code (thường null)
     */
    @JsonProperty("code")
    private String code;
}
