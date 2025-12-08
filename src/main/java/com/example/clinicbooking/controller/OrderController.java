package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Order.OrderOverviewResponse;
import com.example.clinicbooking.DTO.Order.OrderRequest;
import com.example.clinicbooking.DTO.Order.OrderResponse;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.service.Order.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Quản lý các y lệnh/dịch vụ y tế trong hồ sơ ngoại trú")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/overview")
    public ResponseEntity<OrderOverviewResponse> getOrderOverviewMetrics() {
        OrderOverviewResponse response = orderService.getOrderOverviewMetrics();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<OrderResponse>> getOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchDate,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "requestedAt") String SortBy,
            @RequestParam(defaultValue = "DESC") String SortDir) {
        OrderRequest request = new OrderRequest();
        request.setKeyword(keyword);
        request.setStatus(status);
        request.setFindDate(searchDate);
        request.setDoctorId(doctorId);
        request.setSize(size);
        request.setPage(page);
        request.setSortDir(SortDir);
        request.setSortBy(SortBy);

        PaginatedResponseDTO<OrderResponse> response = orderService.AllOrders(request);
        return ResponseEntity.ok(response);
    }
}
