package com.example.clinicbooking.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SepayConfig {

    @Value("${sepay.api.key:}")
    private String apiKey;

    //Số tài khoản ngân hàng nhận tiền
    @Value("${sepay.account.number:}")
    private String accountNumber;

    //Tên chủ tài khoản (IN HOA, không dấu)
    @Value("${sepay.account.name:}")
    private String accountName;

    /**
     * Mã ngân hàng (BIN code) theo Napas
     * - 970422: MBBank
     */
    @Value("${sepay.bank.code:}")
    private String bankCode;

    @Value("${sepay.bank.name:}")
    private String bankName;

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty()
                && accountNumber != null && !accountNumber.isEmpty()
                && bankCode != null && !bankCode.isEmpty();
    }
}
