package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Payment.DetailForCashier.PaymentDetailResponse;
import com.example.clinicbooking.DTO.Payment.PaymentProcessRequest;
import com.example.clinicbooking.DTO.Payment.PaymentResponse;
import com.example.clinicbooking.DTO.Payment.PaymentSearchRequest;
import com.example.clinicbooking.DTO.Payment.QrCodeResponse;
import com.example.clinicbooking.DTO.Payment.SepayWebhookRequest;
import com.example.clinicbooking.entity.Payment;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.service.Payment.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Payment", description = "Quản lý thanh toán")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    // Lấy danh sách phiếu thanh toán với phân trang và lọc
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<PaymentResponse>> getDoctorMedicalRecords(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String SortBy,
            @RequestParam(defaultValue = "ASC") String SortDir) {
        PaymentSearchRequest request = new PaymentSearchRequest();
        request.setQuery(keyword);
        request.setStatus(status);
        request.setSearchDate(searchDate);
        request.setSize(size);
        request.setPage(page);
        request.setSortDir(SortDir);
        request.setSortBy(SortBy);

        PaginatedResponseDTO<PaymentResponse> response = paymentService.searchPayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{paymentId}/detail")
    public ResponseEntity<ApiResponse<PaymentDetailResponse>> getPaymentDetail(@PathVariable Integer paymentId) {
        PaymentDetailResponse response = paymentService.getPaymentDetails(paymentId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách chi tiết thanh toán thành công!", response));
    }

    // API để thực hiện thanh toán cho một hóa đơn.
    @PostMapping("/{paymentId}/pay")
    public ResponseEntity<ApiResponse<?>> processPayment(
            @PathVariable Integer paymentId,
            @RequestBody PaymentProcessRequest request) {

        // Xử lý logic tại Service Layer
        Payment updatedPayment = paymentService.processPayment(paymentId, request);

        return ResponseEntity.ok(new ApiResponse<>(true, "Thanh toán thành công!", updatedPayment));
    }

    @GetMapping("/{paymentId}/invoice")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable Integer paymentId) {
        try {
            byte[] pdfBytes = paymentService.generateInvoiceReport(paymentId);

            // Thiết lập Headers
            HttpHeaders headers = new HttpHeaders();
            // Đặt Content-Type là application/pdf
            headers.setContentType(MediaType.APPLICATION_PDF);
            LocalDate currentDate = LocalDate.now();
            String filename = "invoice_" + paymentId + currentDate + ".pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (JRException | InvalidInputException e) {
            // Xử lý lỗi và trả về thông báo phù hợp
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage().getBytes());
        }
    }

    // ============================================
    // PHẦN MỚI: QR CODE PAYMENT ENDPOINTS
    // ============================================

    /**
     * API lấy mã QR cho thanh toán chuyển khoản
     * 
     * FLOW:
     * 1. Frontend (Cashier) gọi API này khi chọn phương thức "Chuyển khoản"
     * 2. Backend tạo QR Code URL từ VietQR API
     * 3. Frontend hiển thị QR trong modal
     * 4. Bệnh nhân quét QR bằng app ngân hàng
     * 
     * @param paymentId ID phiếu thanh toán
     * @return QrCodeResponse chứa URL QR và thông tin ngân hàng
     */
    @GetMapping("/{paymentId}/qr-code")
    public ResponseEntity<ApiResponse<QrCodeResponse>> getPaymentQrCode(@PathVariable Integer paymentId) {
        try {
            QrCodeResponse qrCode = paymentService.generateQrCode(paymentId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Tạo mã QR thành công!", qrCode));
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Webhook endpoint nhận callback từ SePay khi có giao dịch chuyển khoản
     * 
     * FLOW:
     * 1. Bệnh nhân quét QR và chuyển khoản
     * 2. SePay nhận thông báo từ ngân hàng
     * 3. SePay gửi POST request tới đây
     * 4. Backend parse paymentCode từ nội dung chuyển khoản
     * 5. Cập nhật Payment status = PAID
     * 6. Trả về 200 OK cho SePay
     *
     * @return ResponseEntity với status 200 nếu thành công
     */
    @PostMapping("/sepay-webhook")
    public ResponseEntity<String> handleSepayWebhook(@RequestBody SepayWebhookRequest request) {
        try {
            // Gọi service để xử lý webhook
            boolean success = paymentService.handleSepayWebhook(request);

            if (success) {
                return ResponseEntity.ok("Webhook processed successfully");
            } else {
                return ResponseEntity.ok("Webhook received but not processed");
            }
        } catch (Exception e) {
            System.err.println("ERROR in webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("Webhook error");
        }
    }
}
