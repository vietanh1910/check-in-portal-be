package com.example.hunter_point.service;

import com.example.hunter_point.dto.request.CampaignRequest;
import com.example.hunter_point.dto.response.CampaignResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CampaignService {
    // --- CREATE ---
    @Transactional
    CampaignResponse createCampaign(CampaignRequest requestDTO, Long allocatorId);

    // --- READ ---
    CampaignResponse getCampaignById(Long id);

    List<CampaignResponse> getAllCampaigns();

    List<CampaignResponse> getCampaignsByAllocator(Long allocatorId);

    // --- UPDATE ---
    @Transactional
    CampaignResponse updateCampaign(Long id, CampaignRequest requestDTO);

    // --- DELETE ---
    void deleteCampaign(Long id);

    Long getAllocatorIdByCampaignId(Long campaignId);
}
