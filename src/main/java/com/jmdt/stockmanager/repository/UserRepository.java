package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.User;
import com.jmdt.stockmanager.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    List<User> findByBusinessId(Long businessId);
    
    List<User> findByRole(UserRole role);
    
    List<User> findByBusinessIdAndRole(Long businessId, UserRole role);
}
