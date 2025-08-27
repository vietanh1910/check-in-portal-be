package com.example.hunter_point.controller;

import com.example.hunter_point.dto.request.VoucherRequest;
import com.example.hunter_point.dto.response.VoucherResponse;
import com.example.hunter_point.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<List<VoucherResponse>> getAll() {
        return ResponseEntity.ok(voucherService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(voucherService.getById(id));
    }

    @PostMapping
    public ResponseEntity<VoucherResponse> create(@RequestBody VoucherRequest request) {
        return ResponseEntity.ok(voucherService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherResponse> update(@PathVariable Long id, @RequestBody VoucherRequest request) {
        return ResponseEntity.ok(voucherService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        voucherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

