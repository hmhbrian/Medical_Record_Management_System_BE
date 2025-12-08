package com.example.clinicbooking.service.Order;

import com.example.clinicbooking.DTO.Order.OrderOverviewResponse;
import com.example.clinicbooking.DTO.Order.OrderRequest;
import com.example.clinicbooking.DTO.Order.OrderResponse;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.entity.Order;
import com.example.clinicbooking.entity.ServiceStatus;
import com.example.clinicbooking.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                responseOrders);
    }

    /**
     * Lấy tổng quan y lệnh cho Admin
     * Bao gồm các thống kê:
     * - Tổng số y lệnh (tất cả thời gian)
     * - Tổng số y lệnh trong ngày
     * - Y lệnh chờ xử lý trong ngày (status = PAID)
     * - Y lệnh đang thực hiện trong ngày (status = IN_PROGRESS)
     * - Y lệnh hoàn thành trong ngày (status = COMPLETED)
     * 
     * @param searchDateStr Ngày cần thống kê (định dạng yyyy-MM-dd), null = ngày
     *                      hiện tại
     * @return OrderOverviewResponse chứa các thống kê tổng quan
     */
    public OrderOverviewResponse getOrderOverviewMetrics(String searchDateStr) {
        // --- 1. Xác định ngày thống kê ---
        LocalDate searchDate = LocalDate.now();
        if (searchDateStr != null && !searchDateStr.isEmpty()) {
            searchDate = LocalDate.parse(searchDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        }

        // --- 2. Truy vấn dữ liệu ---

        // 2.1. Tổng số y lệnh (tất cả thời gian)
        Long totalOrders = orderRepo.count();

        // 2.2. Tổng số y lệnh trong ngày (theo requestedAt)
        Long totalOrdersToday = orderRepo.count(OrderSpecification.byRequestedDate(searchDate));

        // 2.3. Y lệnh chờ xử lý trong ngày (status = PAID)
        Long pendingOrdersToday = orderRepo.count(
                OrderSpecification.byStatusAndDate(ServiceStatus.PAID, searchDate));

        // 2.4. Y lệnh đang thực hiện trong ngày (status = IN_PROGRESS)
        Long inProgressOrdersToday = orderRepo.count(
                OrderSpecification.byStatusAndDate(ServiceStatus.IN_PROGRESS, searchDate));

        // 2.5. Y lệnh hoàn thành trong ngày (status = COMPLETED)
        Long completedOrdersToday = orderRepo.count(
                OrderSpecification.byStatusAndDate(ServiceStatus.COMPLETED, searchDate));

        // --- 3. Đóng gói và trả về DTO ---
        return new OrderOverviewResponse(
                totalOrders,
                totalOrdersToday,
                pendingOrdersToday,
                inProgressOrdersToday,
                completedOrdersToday);
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

        // 3.Thông tin thời gian chỉ định
        dto.setRequestedAt(order.getRequestedAt());
        dto.setCompletedAt(order.getCompletedAt());
        // --- TÍNH TIME EXECUTION ---
        LocalDateTime startTime = order.getRequestedAt();
        Duration duration;
        // Nếu dịch vụ ĐÃ hoàn thành (có CompletedAt)
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
        if (order.getStaffName() != null)
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
        long days = seconds / (24 * 3600); // Lấy số ngày
        seconds %= (24 * 3600); // Cập nhật lại seconds còn lại sau khi lấy ngày
        long hours = seconds / 3600; // Lấy số giờ
        seconds %= 3600; // Cập nhật lại seconds còn lại sau khi lấy giờ
        long minutes = seconds / 60; // Lấy số phút

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
