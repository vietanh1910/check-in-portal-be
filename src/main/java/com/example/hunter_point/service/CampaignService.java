package com.example.hunter_point.service;

import com.example.hunter_point.dto.request.CampaignRequest;
import com.example.hunter_point.dto.response.CampaignResponse;
import com.example.hunter_point.utils.response.GetDetailResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CampaignService {
    // --- CREATE ---
    SimpleResponse createCampaign(CampaignRequest requestDTO);

    // --- READ ---
    GetDetailResponse<CampaignResponse> getCampaignById(Long id);

    ListResponse<CampaignResponse> getAllCampaigns(CampaignRequest requestDTO);

    // --- UPDATE ---
    CampaignResponse updateCampaign(Long id, CampaignRequest requestDTO);

    // --- DELETE ---
    void deleteCampaign(Long id);

    Long getAllocatorIdByCampaignId(Long campaignId);
}
