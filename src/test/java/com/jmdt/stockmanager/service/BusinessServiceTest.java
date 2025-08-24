package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.enums.PlanType;
import com.jmdt.stockmanager.exception.ResourceNotFoundException;
import com.jmdt.stockmanager.models.Business;
import com.jmdt.stockmanager.repository.BusinessRepository;
import com.jmdt.stockmanager.service.impl.BusinessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest {

    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private BusinessServiceImpl businessService;

    private Business testBusiness;

    @BeforeEach
    void setUp() {
        testBusiness = new Business();
        testBusiness.setId(1L);
        testBusiness.setName("Test Business");
        testBusiness.setContactEmail("test@business.com");
        testBusiness.setPhone("1234567890");
        testBusiness.setAddress("123 Test Street");
        testBusiness.setPlan(PlanType.FREE);
        testBusiness.setTimezone("UTC");
        testBusiness.setCreatedAt(LocalDateTime.now());
        testBusiness.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateBusiness() {
        // Given
        when(businessRepository.save(any(Business.class))).thenReturn(testBusiness);

        // When
        Business result = businessService.createBusiness(testBusiness);

        // Then
        assertNotNull(result);
        assertEquals("Test Business", result.getName());
        assertEquals(PlanType.FREE, result.getPlan());
        verify(businessRepository, times(1)).save(any(Business.class));
    }

    @Test
    void testGetBusinessById_Found() {
        // Given
        when(businessRepository.findById(1L)).thenReturn(Optional.of(testBusiness));

        // When
        Optional<Business> result = businessService.getBusinessById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Business", result.get().getName());
        verify(businessRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBusinessById_NotFound() {
        // Given
        when(businessRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Optional<Business> result = businessService.getBusinessById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(businessRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateBusiness_Success() {
        // Given
        Business updatedBusiness = new Business();
        updatedBusiness.setName("Updated Business");
        updatedBusiness.setContactEmail("updated@business.com");
        updatedBusiness.setPlan(PlanType.MONTHLY);

        when(businessRepository.findById(1L)).thenReturn(Optional.of(testBusiness));
        when(businessRepository.save(any(Business.class))).thenReturn(testBusiness);

        // When
        Business result = businessService.updateBusiness(1L, updatedBusiness);

        // Then
        assertNotNull(result);
        verify(businessRepository, times(1)).findById(1L);
        verify(businessRepository, times(1)).save(any(Business.class));
    }

    @Test
    void testUpdateBusiness_NotFound() {
        // Given
        when(businessRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            businessService.updateBusiness(999L, testBusiness);
        });
        verify(businessRepository, times(1)).findById(999L);
        verify(businessRepository, never()).save(any(Business.class));
    }

    @Test
    void testGetAllBusinesses() {
        // Given
        List<Business> businesses = Arrays.asList(testBusiness, new Business());
        when(businessRepository.findAll()).thenReturn(businesses);

        // When
        List<Business> result = businessService.getAllBusinesses();

        // Then
        assertEquals(2, result.size());
        verify(businessRepository, times(1)).findAll();
    }

    @Test
    void testGetBusinessesByPlan() {
        // Given
        Business monthlyBusiness = new Business();
        monthlyBusiness.setPlan(PlanType.MONTHLY);
        
        List<Business> allBusinesses = Arrays.asList(testBusiness, monthlyBusiness);
        when(businessRepository.findAll()).thenReturn(allBusinesses);

        // When
        List<Business> result = businessService.getBusinessesByPlan(PlanType.FREE);

        // Then
        assertEquals(1, result.size());
        assertEquals(PlanType.FREE, result.get(0).getPlan());
        verify(businessRepository, times(1)).findAll();
    }

    @Test
    void testDeleteBusiness_Success() {
        // Given
        when(businessRepository.findById(1L)).thenReturn(Optional.of(testBusiness));
        doNothing().when(businessRepository).delete(any(Business.class));

        // When
        businessService.deleteBusiness(1L);

        // Then
        verify(businessRepository, times(1)).findById(1L);
        verify(businessRepository, times(1)).delete(testBusiness);
    }

    @Test
    void testDeleteBusiness_NotFound() {
        // Given
        when(businessRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            businessService.deleteBusiness(999L);
        });
        verify(businessRepository, times(1)).findById(999L);
        verify(businessRepository, never()).delete(any(Business.class));
    }

    @Test
    void testExistsByName() {
        // Given
        when(businessRepository.findByName("Test Business")).thenReturn(Optional.of(testBusiness));
        when(businessRepository.findByName("Non-existent")).thenReturn(Optional.empty());

        // When & Then
        assertTrue(businessService.existsByName("Test Business"));
        assertFalse(businessService.existsByName("Non-existent"));
        
        verify(businessRepository, times(1)).findByName("Test Business");
        verify(businessRepository, times(1)).findByName("Non-existent");
    }

    @Test
    void testExistsByContactEmail() {
        // Given
        when(businessRepository.findByContactEmail("test@business.com")).thenReturn(Optional.of(testBusiness));
        when(businessRepository.findByContactEmail("nonexistent@email.com")).thenReturn(Optional.empty());

        // When & Then
        assertTrue(businessService.existsByContactEmail("test@business.com"));
        assertFalse(businessService.existsByContactEmail("nonexistent@email.com"));
        
        verify(businessRepository, times(1)).findByContactEmail("test@business.com");
        verify(businessRepository, times(1)).findByContactEmail("nonexistent@email.com");
    }
}
