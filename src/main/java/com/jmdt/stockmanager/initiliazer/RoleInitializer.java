package com.jmdt.stockmanager.initiliazer;

import com.jmdt.stockmanager.models.Role;
import com.jmdt.stockmanager.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.jmdt.stockmanager.constants.AppConstants.*;

@Component
@RequiredArgsConstructor  // Lombok for constructor injection
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional  // Ensures all operations succeed or roll back
    public void run(String... args) {
        createRoleIfNotExists(ROLE_ADMIN_ID, ROLE_ADMIN_NAME);
        createRoleIfNotExists(ROLE_MANAGER_ID, ROLE_MANAGER_NAME);}

    private void createRoleIfNotExists(Integer id, String name) {
        boolean roleExists = roleRepository.existsById(id);
        if (!roleExists) {
            Role role = new Role();
            role.setRoleId(id);
            role.setRoleName(name);
            roleRepository.save(role);
        }
    }
}
