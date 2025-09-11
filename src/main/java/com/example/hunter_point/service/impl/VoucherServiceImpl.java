package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.request.VoucherRequest;
import com.example.hunter_point.dto.response.VoucherResponse;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.entity.Voucher;
import com.example.hunter_point.entity.enums.RedemptionStatus;
import com.example.hunter_point.entity.enums.VoucherStatus;
import com.example.hunter_point.entity.RedemptionHistory;
import com.example.hunter_point.exception.InsufficientPointsException;
import com.example.hunter_point.exception.ResourceNotFoundException;
import com.example.hunter_point.exception.VoucherOutOfStockException;
import com.example.hunter_point.repository.RedemptionHistoryRepository;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.repository.VoucherRepository;
import com.example.hunter_point.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final RedemptionHistoryRepository redemptionHistoryRepository;

    @Override
    public List<VoucherResponse> getAll() {
        return voucherRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // PHIÊN BẢN HOÀN CHỈNH
    @Override
    @Transactional
    public boolean redeem(Long voucherId, Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUserId));

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found with id: " + voucherId));

        // 1. KIỂM TRA QUAN TRỌNG: Người dùng đã đổi voucher này bao giờ chưa?
        if (redemptionHistoryRepository.countByUserIdAndVoucherId(currentUserId, voucherId) > 0) {
            throw new IllegalStateException("Bạn đã đổi voucher này rồi.");
        }

        // 2. Kiểm tra số lượng
        int quantity = voucher.getQuantity() != null ? voucher.getQuantity() : 0;
        int claimed = voucher.getClaimed() != null ? voucher.getClaimed() : 0;
        if (quantity > 0 && claimed >= quantity) {
            throw new VoucherOutOfStockException("Voucher đã hết lượt đổi.");
        }

        // 3. Kiểm tra điểm
        int userPoints = user.getPoints() != null ? user.getPoints() : 0;
        int pointCost = voucher.getPointCost() != null ? voucher.getPointCost() : 0;
        if (userPoints < pointCost) {
            throw new InsufficientPointsException("Bạn không đủ điểm để đổi voucher này.");
        }

        // Thực hiện cập nhật
        user.setPoints(userPoints - pointCost);
        voucher.setClaimed(claimed + 1);
        voucher.setUpdatedAt(LocalDateTime.now());

        // Tạo bản ghi lịch sử mới với trạng thái ban đầu là NOT_USED
        RedemptionHistory historyEntry = RedemptionHistory.builder()
                .user(user)
                .voucher(voucher)
                .redeemedAt(LocalDateTime.now())
                .status(RedemptionStatus.NOT_USED)
                .build();

        // Lưu tất cả các thay đổi vào DB
        userRepository.save(user);
        voucherRepository.save(voucher);
        redemptionHistoryRepository.save(historyEntry);

        System.out.println("Redeem successful: userId=" + currentUserId + ", voucherId=" + voucherId);
        return true;
    }

    // PHƯƠNG THỨC MỚI (Đã đúng trong code của bạn)
    @Override
    @Transactional(readOnly = true)
    public List<VoucherResponse> getAvailableVouchersForUser(Long userId) {
        List<Voucher> availableVouchers = voucherRepository.findAvailableVouchersForUser(userId);
        return availableVouchers.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // CÁC PHƯƠNG THỨC CRUD KHÁC (giữ nguyên, đã ổn)
    @Override
    public VoucherResponse getById(Long id) {
        return voucherRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found with id: " + id));
    }

    @Override
    public VoucherResponse create(VoucherRequest request) {
        LocalDateTime now = LocalDateTime.now();
        if (request.getEndDate() != null && request.getEndDate().isBefore(now)) {
            throw new IllegalArgumentException("End date must be after current time");
        }
        Voucher voucher = toEntity(request);
        if (request.getStartDate() != null && request.getStartDate().isAfter(now)) {
            voucher.setStatus(VoucherStatus.INACTIVE);
        } else {
            voucher.setStatus(VoucherStatus.ACTIVE);
        }
        voucher.setCreatedAt(now);
        voucher.setUpdatedAt(now);
        return toResponse(voucherRepository.save(voucher));
    }

    @Override
    public VoucherResponse update(Long id, VoucherRequest request) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found with id: " + id));
        LocalDateTime now = LocalDateTime.now();
        if (request.getEndDate() != null && request.getEndDate().isBefore(now)) {
            throw new IllegalArgumentException("End date must be after current time");
        }
        voucher.setTitle(request.getTitle());
        voucher.setDescription(request.getDescription());
        voucher.setDiscountType(request.getDiscountType());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setMinOrderValue(request.getMinOrderValue());
        voucher.setMaxDiscount(request.getMaxDiscount());
        voucher.setPointCost(request.getPointCost());
        voucher.setQuantity(request.getQuantity());
        voucher.setClaimed(request.getClaimed());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        voucher.setIsPublished(request.getIsPublished());
        voucher.setUserId(request.getUserId());
        if (request.getStartDate() != null && request.getStartDate().isAfter(now)) {
            voucher.setStatus(VoucherStatus.INACTIVE);
        } else {
            voucher.setStatus(VoucherStatus.ACTIVE);
        }
        voucher.setUpdatedAt(now);
        return toResponse(voucherRepository.save(voucher));
    }

    @Override
    public void delete(Long id) {
        if (!voucherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Voucher not found with id: " + id);
        }
        voucherRepository.deleteById(id);
    }

    // HÀM HELPER (giữ nguyên, đã ổn)
    private VoucherResponse toResponse(Voucher v) {
        // Cân nhắc dùng Builder ở đây để code gọn hơn
        return VoucherResponse.builder()
                .id(v.getId())
                .title(v.getTitle())
                .description(v.getDescription())
                .discountType(v.getDiscountType())
                .discountValue(v.getDiscountValue())
                .minOrderValue(v.getMinOrderValue())
                .maxDiscount(v.getMaxDiscount())
                .pointCost(v.getPointCost())
                .quantity(v.getQuantity())
                .claimed(v.getClaimed())
                .startDate(v.getStartDate())
                .endDate(v.getEndDate())
                .status(v.getStatus())
                .isPublished(v.getIsPublished())
                .userId(v.getUserId())
                .createdAt(v.getCreatedAt())
                .updatedAt(v.getUpdatedAt())
                .imageUrl(v.getImageUrl()) // Đảm bảo có imageUrl
                .build();
    }

    private Voucher toEntity(VoucherRequest request) {
        return Voucher.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .minOrderValue(request.getMinOrderValue())
                .maxDiscount(request.getMaxDiscount())
                .pointCost(request.getPointCost())
                .quantity(request.getQuantity())
                .claimed(request.getClaimed())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(VoucherStatus.ACTIVE)
                .isPublished(request.getIsPublished())
                .userId(request.getUserId())
                .imageUrl(request.getImageUrl())
                .build();
    }
}