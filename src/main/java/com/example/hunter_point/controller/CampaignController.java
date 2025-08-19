package com.example.hunter_point.controller;


import com.example.hunter_point.dto.request.CampaignRequest;
import com.example.hunter_point.dto.response.CampaignResponse;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.security.UserDetailsImpl;
import com.example.hunter_point.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping
    @PreAuthorize("hasRole('ALLOCATOR')")
    public ResponseEntity<CampaignResponse> createCampaign(@RequestBody CampaignRequest requestDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CampaignResponse createdCampaign = campaignService.createCampaign(requestDTO, userDetails.getId());
        return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CampaignResponse>> getAllCampaigns() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<CampaignResponse> campaigns;
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ERole.ROLE_ADMIN.name()))) {
            campaigns = campaignService.getAllCampaigns();
        } else {
            campaigns = campaignService.getCampaignsByAllocator(userDetails.getId());
        }
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CampaignResponse> getCampaignById(@PathVariable Long id) {
        CampaignResponse campaign = campaignService.getCampaignById(id);
        return ResponseEntity.ok(campaign);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ALLOCATOR', 'ADMIN')")
    public ResponseEntity<CampaignResponse> updateCampaign(@PathVariable Long id, @RequestBody CampaignRequest requestDTO) {
        checkOwnership(id); // Kiểm tra quyền sở hữu
        CampaignResponse updatedCampaign = campaignService.updateCampaign(id, requestDTO);
        return ResponseEntity.ok(updatedCampaign);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ALLOCATOR', 'ADMIN')")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        checkOwnership(id); // Kiểm tra quyền sở hữu
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }

    private void checkOwnership(Long campaignId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(ERole.ROLE_ADMIN.name()))) {
            return;
        }

        Long allocatorIdOfCampaign = campaignService.getAllocatorIdByCampaignId(campaignId);
        if (!allocatorIdOfCampaign.equals(userDetails.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this campaign");
        }
    }
}
