package com.example.clinicbooking.service.Order;

import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordSearchAllRequest;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordSummaryDTO;
import com.example.clinicbooking.DTO.Order.OrderOverviewResponse;
import com.example.clinicbooking.DTO.Order.OrderRequest;
import com.example.clinicbooking.DTO.Order.OrderResponse;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Patient.PatientSummary;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.Order;
import com.example.clinicbooking.repository.DoctorRepository;
import com.example.clinicbooking.repository.MedicalRecordRepository;
import com.example.clinicbooking.repository.OrderRepository;
import com.example.clinicbooking.repository.PatientRepository;
import com.example.clinicbooking.service.MedicalRecord.AllMedicalRecordSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepo;

    // Lấy danh sách tất cả chỉ định dịch vụ với phân trang, sắp xếp và lọc
    public PaginatedResponseDTO<OrderResponse> AllOrders(
            OrderRequest request) {

        // 1. Chuẩn bị phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // 2. Xây dựng Specification (logic lọc)
        Specification<Order> spec = OrderSpecification.filterOrders(request);

        // 3. Thực hiện truy vấn
        Page<Order> orderPage = orderRepo.findAll(spec, pageable);

        // 4. Ánh xạ (Mapping) Entity sang Response DTO
        List<OrderResponse> responseOrders = orderPage.getContent().stream()
                .map(this::covertToSummaryResponse) // Sử dụng hàm covertToResponse để chuyển đổi
                .collect(Collectors.toList());

        // 5. Trả về Paginated Response
        return new PaginatedResponseDTO<OrderResponse>(
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                responseOrders
        );
    }

    public OrderOverviewResponse getOrderOverviewMetrics() {
        OrderOverviewResponse response = new OrderOverviewResponse();

        // 1. Tổng số Y lệnh Đang Chờ
        response.setTotalPendingOrders(calculateTotalPendingOrders());

        // 2. Y lệnh Quá hạn (> 24h)
        response.setTotalOverdueOrders(calculateOverdueOrders());

        // 3. Tổng số Y lệnh Hoàn thành Hôm nay
        response.setTotalCompletedToday(calculateCompletedToday());

        // 4. Thời gian Xử lý Trung bình (TAT)
        response.setAverageTAT(calculateAverageTAT());

        return response;
    }

    // --- 1. Tổng số Y lệnh Đang Chờ (PAID, IN_PROGRESS) ---
    private long calculateTotalPendingOrders() {
        Specification<Order> pendingSpec = OrderSpecification.isPending();
        return orderRepo.count(pendingSpec);
    }

    // --- 2. Y lệnh Quá hạn (> 24h) ---
    private long calculateOverdueOrders() {
        List<Order> pendingOrders = orderRepo.findAll(OrderSpecification.isPending());
        long overdueCount = 0;
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);

        for (Order order : pendingOrders) {
            // Lọc các order đang chờ đã được yêu cầu quá 24h trước
            if (order.getRequestedAt().isBefore(twentyFourHoursAgo)) {
                overdueCount++;
            }
        }
        return overdueCount;
    }

    // --- 3. Tổng số Y lệnh Hoàn thành Hôm nay ---
    private long calculateCompletedToday() {
        Specification<Order> completedTodaySpec = OrderSpecification.isCompletedToday();
        return orderRepo.count(completedTodaySpec);
    }

    // --- 4. Thời gian Xử lý Trung bình (Average TAT) ---
    private double calculateAverageTAT() {
        Specification<Order> completedSpec = OrderSpecification.isCompleted();
        List<Order> completedOrders = orderRepo.findAll(completedSpec);

        if (completedOrders.isEmpty()) {
            return 0.0; // Trả về 0 nếu không có y lệnh nào hoàn thành
        }

        long totalMinutes = 0;

        for (Order order : completedOrders) {
            // Đảm bảo cả hai mốc thời gian đều tồn tại trước khi tính
            if (order.getRequestedAt() != null && order.getCompletedAt() != null) {
                Duration duration = Duration.between(order.getRequestedAt(), order.getCompletedAt());
                totalMinutes += duration.toMinutes();
            }
        }

        // Tính TAT trung bình (tính theo Giờ)
        double averageMinutes = (double) totalMinutes / completedOrders.size();
        return averageMinutes / 60.0;
    }

    private OrderResponse covertToSummaryResponse(Order order) {

        OrderResponse dto = new OrderResponse();

        // 1. Thông tin cơ bản
        dto.setOrderId(order.getId().getOrderId());
        dto.setServiceType(order.getId().getServiceType());
        dto.setServiceName(order.getServiceName());
        dto.setRecordCode(order.getMedicalRecord().getCode());
        dto.setStatus(order.getStatus().name());

        // 2. Thông tin Bác sĩ
        dto.setDoctorName(order.getDoctor().getStaff().getUser().getFullname());
        dto.setDoctorCode(order.getDoctor().getDoctorcode());
        dto.setSpecialty(order.getDoctor().getSpecialty().getName());

        //3.Thông tin thời gian chỉ định
        dto.setRequestedAt(order.getRequestedAt());
        dto.setCompletedAt(order.getCompletedAt());
        // --- TÍNH TIME EXECUTION ---
        LocalDateTime startTime = order.getRequestedAt();
        Duration duration;
        //Nếu dịch vụ ĐÃ hoàn thành (có CompletedAt)
        if (order.getCompletedAt() != null) {
            LocalDateTime endTime = order.getCompletedAt();
            duration = Duration.between(startTime, endTime);
        }
        // Nếu dịch vụ CHƯA hoàn thành
        else {
            LocalDateTime now = LocalDateTime.now();
            duration = Duration.between(startTime, now);
        }

        // Định dạng thời gian thực hiện thành chuỗi dễ đọc
        dto.setTimeExecution(formatDuration(duration));
        dto.setResult(order.getResult());

        // 4. Thông tin nhân viên thực hiện
        dto.setStaffCode(order.getStaffCode());
        if(order.getStaffName() != null)
            dto.setStaffName(order.getStaffName());
        else
            dto.setStaffName("Chưa phân công");

        return dto;
    }

    private String formatDuration(Duration duration) {
        if (duration == null || duration.isNegative() || duration.isZero()) {
            return "0 phút";
        }

        long seconds = duration.getSeconds();
        long days = seconds / (24 * 3600); //Lấy số ngày
        seconds %= (24 * 3600);            //Cập nhật lại seconds còn lại sau khi lấy ngày
        long hours = seconds / 3600;       //Lấy số giờ
        seconds %= 3600;                   //Cập nhật lại seconds còn lại sau khi lấy giờ
        long minutes = seconds / 60;       //Lấy số phút

        StringBuilder sb = new StringBuilder();

        if (days > 0) {
            sb.append(days).append(" ngày ");
        }
        if (hours > 0) {
            sb.append(hours).append(" giờ ");
        }
        if (minutes > 0 || sb.length() == 0) {
            // Nếu không có ngày hay giờ, bắt buộc phải hiển thị phút
            sb.append(minutes).append(" phút");
        }

        return sb.toString().trim();
    }
}
