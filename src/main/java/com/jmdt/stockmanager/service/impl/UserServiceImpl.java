package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.enums.UserRole;
import com.jmdt.stockmanager.exception.StockManagerException;
import com.jmdt.stockmanager.models.Business;
import com.jmdt.stockmanager.models.User;
import com.jmdt.stockmanager.payloads.UserDto;
import com.jmdt.stockmanager.repository.BusinessRepository;
import com.jmdt.stockmanager.repository.UserRepository;
import com.jmdt.stockmanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto signUp(UserDto userDto, String userRole) {

        //if user already exist with this email then return with message
        Optional<User> duplicateUser = userRepository.findByEmail(userDto.getEmail());
        if(duplicateUser.isPresent()){
            throw new StockManagerException("Hi "+ userDto.getFirstName() + ", this email already exists");
        }

        User user = new User();
        user.setName(userDto.getFirstName() + " " + (userDto.getLastName() != null ? userDto.getLastName() : ""));
        user.setEmail(userDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));

        // Set role based on userRole parameter
        UserRole role;
        switch (userRole.toUpperCase()) {
            case "ADMIN":
                role = UserRole.ADMIN;
                break;
            case "MANAGER":
                role = UserRole.MANAGER;
                break;
            case "STAFF":
                role = UserRole.STAFF;
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + userRole);
        }
        user.setRole(role);

        // For now, we'll need to handle business assignment separately
        // This is a simplified version - in a real scenario, you'd need to determine
        // which business the user belongs to during signup
        // Assign business to user during signup
        if (userDto.getBusinessEmail() != null) {
            Optional<Business> businessOpt = businessRepository.findByContactEmail(userDto.getBusinessEmail());
            if (businessOpt.isPresent()) {
                user.setBusiness(businessOpt.get());
            } else {
                throw new StockManagerException("Business not found with ID: " + userDto.getBusinessEmail());
            }
        }

        log.info("Creating user with role: {}", role);

        User newUser = this.userRepository.save(user);

        // Map back to UserDto
        UserDto resultDto = new UserDto();
        resultDto.setFirstName(newUser.getName().split(" ")[0]);
        if (newUser.getName().split(" ").length > 1) {
            resultDto.setLastName(newUser.getName().split(" ", 2)[1]);
        }
        resultDto.setEmail(newUser.getEmail());
        
        return resultDto;
    }
}
