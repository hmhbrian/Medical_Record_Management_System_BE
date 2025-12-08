package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Bed.BedRequest;
import com.example.clinicbooking.DTO.Bed.BedResponse;
import com.example.clinicbooking.DTO.Bed.DepartmentBedRp;
import com.example.clinicbooking.DTO.Bed.OverViewResponse;
import com.example.clinicbooking.entity.Bed;
import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.Room;
import com.example.clinicbooking.entity.RoomTypes;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.BedRepository;
import com.example.clinicbooking.repository.roomRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BedService {
    private final BedRepository bedRepo;
    private final roomRepository roomRepo;

    // Danh sách Bed (lọc + phân trang)
    public Page<BedResponse> listBeds(String keyword, Integer status, Integer departmentId,
            Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
                page != null && page > 0 ? page - 1 : 0, // 1-based -> 0-based
                size != null && size > 0 ? size : 10,
                Sort.by(Sort.Direction.DESC, "updatedAt").and(Sort.by(Sort.Direction.DESC, "id")));

        Specification<Bed> spec = buildSpec(keyword, status, departmentId);

        Page<Bed> beds = bedRepo.findAll(spec, pageable);
        List<BedResponse> data = beds.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(data, pageable, beds.getTotalElements());
    }

    private Specification<Bed> buildSpec(String keyword, Integer status, Integer departmentId) {
        return (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();

            // Join Room -> Department -> RoomType
            Join<Bed, Room> roomJ = root.join("room", JoinType.INNER);
            Join<Room, Department> deptJ = roomJ.join("department", JoinType.LEFT);

            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase() + "%";
                Predicate byBedNumber = cb.like(cb.lower(root.get("bedNumber")), like);
                Predicate byRoomName = cb.like(cb.lower(roomJ.get("name")), like);
                Predicate byDeptName = cb.like(cb.lower(deptJ.get("name")), like);
                ps.add(cb.or(byBedNumber, byRoomName, byDeptName));
            }

            if (status != null) {
                ps.add(cb.equal(root.get("status"), status));
            }

            if (departmentId != null) {
                ps.add(cb.equal(deptJ.get("id"), departmentId));
            }

            query.distinct(true);
            return cb.and(ps.toArray(new Predicate[0]));
        };
    }

    // Tạo mới Bed
    public BedResponse createBed(BedRequest req) {
        Room room = roomRepo.findById(req.getRoom_id())
                .orElseThrow(() -> new InvalidInputException("Room not found"));

        Bed bed = new Bed();
        bed.setRoom(room);
        bed.setBedNumber(req.getBed_number());
        bed.setBedFee(req.getBed_fee());
        bed.setStatus(req.getStatus() == null ? 1 : req.getStatus()); // default Available
        bed.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        Bed saved = bedRepo.save(bed);
        return toResponse(saved);
    }

    // Overview
    public OverViewResponse overview() {
        var totals = bedRepo.aggregateTotals();

        OverViewResponse rp = new OverViewResponse();
        rp.setSumBed((int) totals.getTotal());
        rp.setAvailableBeds((int) totals.getAvailable()); // GiuongTrong
        rp.setOccupiedBeds((int) totals.getOccupied()); // GiuongDangSuDung
        rp.setMaintenanceBeds((int) totals.getMaintenance()); // GiuongDangBaoTri

        var byDept = bedRepo.aggregateByDepartment().stream().map(agg -> {
            DepartmentBedRp d = new DepartmentBedRp();
            d.setDepartmentName(agg.getDeptName());
            d.setGiuongDangSuDung((int) agg.getOccupied());
            d.setTongGiuongTrongKhoa((int) agg.getTotal());
            double tyle = agg.getTotal() == 0 ? 0.0 : (agg.getOccupied() / agg.getTotal());
            d.setTyle(Math.round(tyle)); // làm tròn 2 chữ số
            return d;
        }).collect(Collectors.toList());

        rp.setDepartmentBed(byDept);
        return rp;
    }

    // ======= Mapping & utils =======
    private BedResponse toResponse(Bed b) {
        BedResponse dto = new BedResponse();
        dto.setBed_number(nullSafe(b.getBedNumber()));

        Room room = b.getRoom();
        if (room != null) {
            dto.setRoom_name(room.getName());
            Department d = room.getDepartment();
            dto.setDepartmentName(d != null ? nullSafe(d.getName()) : null);
            RoomTypes t = room.getRoomType();
            dto.setUpdated_at(t != null ? b.getUpdatedAt() : null);

            dto.setBedType_name(t != null ? nullSafe(t.getName()) : null);
        }

        dto.setBedStatus(statusLabel(b.getStatus()));
        dto.setBed_fee(formatVND(b.getBedFee()));
        return dto;
    }

    private String statusLabel(Integer s) {
        if (s == null)
            return "Unknown";
        return switch (s) {
            case 0 -> "Đang sử dụng";
            case 1 -> "Trống";
            case 2 -> "Vệ sinh";
            case 3 -> "Bảo trì";
            default -> "Unknown";
        };
    }

    // "000.000 đ" (dấu . ngăn cách nghìn, không dùng ký hiệu ₫)
    private String formatVND(Double val) {
        if (val == null)
            return null;
        DecimalFormatSymbols sym = new DecimalFormatSymbols();
        sym.setGroupingSeparator('.');
        sym.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0' đ'", sym);
        return df.format(val);
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }
}
