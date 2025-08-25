package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.dto.request.OnboardingRequestDto;
import com.jmdt.stockmanager.enums.PlanType;
import com.jmdt.stockmanager.enums.SubscriptionStatus;
import com.jmdt.stockmanager.enums.UserRole;
import com.jmdt.stockmanager.models.Business;
import com.jmdt.stockmanager.models.Subscription;
import com.jmdt.stockmanager.models.User;
import com.jmdt.stockmanager.repository.BusinessRepository;
import com.jmdt.stockmanager.repository.SubscriptionRepository;
import com.jmdt.stockmanager.repository.UserRepository;
import com.jmdt.stockmanager.service.OnboardingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OnboardingServiceImpl implements OnboardingService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public OnboardingServiceImpl(BusinessRepository businessRepository,
                                 UserRepository userRepository,
                                 SubscriptionRepository subscriptionRepository,
                                 BCryptPasswordEncoder passwordEncoder) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(OnboardingRequestDto dto) {
        Business business = new Business();
        business.setName(dto.getBusinessName());
        business.setContactEmail(dto.getBusinessEmail());
        business.setPlan(PlanType.FREE);
        business = businessRepository.save(business);

        User admin = new User();
        admin.setName(dto.getAdminName());
        admin.setEmail(dto.getAdminEmail());
        admin.setPasswordHash(passwordEncoder.encode(dto.getAdminPassword()));
        admin.setRole(UserRole.ADMIN);
        admin.setBusiness(business);
        userRepository.save(admin);

        Subscription subscription = new Subscription();
        subscription.setBusiness(business);
        subscription.setPlanType(PlanType.FREE);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(subscription);
    }
}

