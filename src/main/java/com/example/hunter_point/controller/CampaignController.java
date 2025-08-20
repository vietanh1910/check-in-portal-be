package com.example.hunter_point.controller;


import com.example.hunter_point.dto.request.CampaignRequest;
import com.example.hunter_point.dto.response.CampaignResponse;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.security.UserDetailsImpl;
import com.example.hunter_point.service.CampaignService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.GetDetailResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;
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
    public SimpleResponse createCampaign(@RequestBody CampaignRequest requestDTO) {
        try {
            return campaignService.createCampaign(requestDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorSimpleResponse("BAD_REQUEST");
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ListResponse<CampaignResponse> getAllCampaigns(CampaignRequest requestDTO) {
        try {
            return campaignService.getAllCampaigns(requestDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorListResponse("BAD_REQUEST");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public GetDetailResponse<CampaignResponse> getCampaignById(@PathVariable Long id) {
        try {
            return campaignService.getCampaignById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorGetDetailResponse("BAD_REQUEST");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ALLOCATOR', 'ADMIN')")
    public ResponseEntity<CampaignResponse> updateCampaign(@PathVariable Long id, @RequestBody CampaignRequest requestDTO) {
        checkOwnership(id);
        CampaignResponse updatedCampaign = campaignService.updateCampaign(id, requestDTO);
        return ResponseEntity.ok(updatedCampaign);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        checkOwnership(id);
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }

    private void checkOwnership(Long campaignId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(ERole.ADMIN.name()))) {
            return;
        }

        Long allocatorIdOfCampaign = campaignService.getAllocatorIdByCampaignId(campaignId);
        if (!allocatorIdOfCampaign.equals(userDetails.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this campaign");
        }
    }
}
