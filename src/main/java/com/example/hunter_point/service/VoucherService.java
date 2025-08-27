package com.example.hunter_point.service;


import com.example.hunter_point.dto.request.VoucherRequest;
import com.example.hunter_point.dto.response.VoucherResponse;

import java.util.List;

public interface VoucherService {
    List<VoucherResponse> getAll();
    VoucherResponse getById(Long id);
    VoucherResponse create(VoucherRequest request);
    VoucherResponse update(Long id, VoucherRequest request);
    void delete(Long id);
}

