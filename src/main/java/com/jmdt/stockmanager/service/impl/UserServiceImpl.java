package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.constants.AppConstants;
import com.jmdt.stockmanager.exception.StockManagerException;
import com.jmdt.stockmanager.models.MyUser;
import com.jmdt.stockmanager.models.Role;
import com.jmdt.stockmanager.payloads.UserDto;
import com.jmdt.stockmanager.repository.RoleRepository;
import com.jmdt.stockmanager.repository.UserRepository;
import com.jmdt.stockmanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.jmdt.stockmanager.constants.AppConstants.ROLE_ADMIN_NAME;
import static com.jmdt.stockmanager.constants.AppConstants.ROLE_MANAGER_NAME;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto signUp(UserDto userDto, String userRole) {

        //if user already exist with this email then return with message
        Optional<MyUser> duplicateUser = userRepository.findByEmail(userDto.getEmail());
        if(duplicateUser.isPresent()){
            throw new StockManagerException("Hi "+ userDto.getFirstName() + ", this email already exists");
        }

        MyUser user = this.modelMapper.map(userDto, MyUser.class);
        String randomId = UUID.randomUUID().toString();
        user.setUserId(randomId);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role;
        /**assign role to new user -
         * 1. get role by role_id(ROLE_CUSTOMER_ID), in role repo ?
         * 2. then add the role in user
         * 3. then save the user*/
        switch (userRole) {
            case ROLE_ADMIN_NAME:
                role = this.roleRepository.findById(AppConstants.ROLE_ADMIN_ID)
                        .orElseThrow(() -> new IllegalStateException("Admin role not found in DB"));
                break;
            case ROLE_MANAGER_NAME:
                role = this.roleRepository.findById(AppConstants.ROLE_MANAGER_ID)
                        .orElseThrow(() -> new IllegalStateException("Manager role not found in DB"));
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + userRole);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        log.info("user.getRoles().add(role) - {} ", user.getRoles());

        MyUser newUser = this.userRepository.save(user);

        return this.modelMapper.map(newUser, UserDto.class);
    }
}
