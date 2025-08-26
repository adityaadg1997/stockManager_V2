package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.dto.request.VendorDTO;

import java.util.List;

public interface VendorService {
    VendorDTO createVendor(VendorDTO dto);

    VendorDTO getVendor(Long id);

    VendorDTO updateVendor(Long id, VendorDTO dto);

    void deleteVendor(Long id);

    List<VendorDTO> getVendorsByBusiness(Long businessId);
}
