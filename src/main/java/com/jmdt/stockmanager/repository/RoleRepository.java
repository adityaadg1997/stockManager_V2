package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
