package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.dto.request.VendorDTO;
import com.jmdt.stockmanager.models.Business;
import com.jmdt.stockmanager.models.Vendor;
import com.jmdt.stockmanager.repository.BusinessRepository;
import com.jmdt.stockmanager.repository.VendorRepository;
import com.jmdt.stockmanager.exception.ResourceNotFoundException;
import com.jmdt.stockmanager.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final BusinessRepository businessRepository;

    @Override
    public VendorDTO createVendor(VendorDTO dto) {
        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        Vendor vendor = new Vendor();
        vendor.setName(dto.getName());
        vendor.setContactEmail(dto.getContactEmail());
        vendor.setPhone(dto.getPhone());
        vendor.setAddress(dto.getAddress());
        vendor.setPaymentTerms(dto.getPaymentTerms());
        vendor.setBusiness(business);

        Vendor saved = vendorRepository.save(vendor);
        return toDTO(saved);
    }

    @Override
    public VendorDTO getVendor(Long id) {
        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        return toDTO(v);
    }

    @Override
    public VendorDTO updateVendor(Long id, VendorDTO dto) {
        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        v.setName(dto.getName());
        v.setContactEmail(dto.getContactEmail());
        v.setPhone(dto.getPhone());
        v.setAddress(dto.getAddress());
        v.setPaymentTerms(dto.getPaymentTerms());

        vendorRepository.save(v);
        return toDTO(v);
    }

    @Override
    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }

    @Override
    public List<VendorDTO> getVendorsByBusiness(Long businessId) {
        return vendorRepository.findByBusinessId(businessId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private VendorDTO toDTO(Vendor v) {
        return VendorDTO.builder()
                .id(v.getId())
                .name(v.getName())
                .contactEmail(v.getContactEmail())
                .phone(v.getPhone())
                .address(v.getAddress())
                .paymentTerms(v.getPaymentTerms())
                .businessId(v.getBusiness() != null ? v.getBusiness().getId() : null)
                .build();
    }
}

