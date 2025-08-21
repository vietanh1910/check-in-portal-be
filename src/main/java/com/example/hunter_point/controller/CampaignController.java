package com.example.hunter_point.controller;

import com.example.hunter_point.dto.request.CampaignRequest;
import com.example.hunter_point.dto.response.CampaignResponse;
import com.example.hunter_point.service.CampaignService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.GetDetailResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public SimpleResponse updateCampaign(@PathVariable Long id, @RequestBody CampaignRequest requestDTO) {
        try {
            return campaignService.updateCampaign(id, requestDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorSimpleResponse("BAD_REQUEST");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public SimpleResponse deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        try {
            return campaignService.deleteCampaign(id);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorSimpleResponse("BAD_REQUEST");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public SimpleResponse approveCampaign(@PathVariable Long id) {
        try {
            return campaignService.approveCampaign(id);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateSuccessSimpleResponse();
        }
    }
}
