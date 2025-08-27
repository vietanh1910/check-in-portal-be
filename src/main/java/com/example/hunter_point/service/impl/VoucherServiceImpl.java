package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.request.VoucherRequest;
import com.example.hunter_point.dto.response.VoucherResponse;
import com.example.hunter_point.entity.Voucher;
import com.example.hunter_point.entity.enums.VoucherStatus;
import com.example.hunter_point.repository.VoucherRepository;
import com.example.hunter_point.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Override
    public List<VoucherResponse> getAll() {
        return voucherRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public VoucherResponse getById(Long id) {
        return voucherRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
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
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

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

        // logic status
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
        voucherRepository.deleteById(id);
    }

    // --- mapping helpers ---
    private VoucherResponse toResponse(Voucher v) {
        VoucherResponse res = new VoucherResponse();
        res.setId(v.getId());
        res.setTitle(v.getTitle());
        res.setDescription(v.getDescription());
        res.setDiscountType(v.getDiscountType());
        res.setDiscountValue(v.getDiscountValue());
        res.setMinOrderValue(v.getMinOrderValue());
        res.setMaxDiscount(v.getMaxDiscount());
        res.setPointCost(v.getPointCost());
        res.setQuantity(v.getQuantity());
        res.setClaimed(v.getClaimed());
        res.setStartDate(v.getStartDate());
        res.setEndDate(v.getEndDate());
        res.setStatus(v.getStatus());
        res.setIsPublished(v.getIsPublished());
        res.setUserId(v.getUserId());
        res.setCreatedAt(v.getCreatedAt());
        res.setUpdatedAt(v.getUpdatedAt());
        return res;
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
                .build();
    }
}

