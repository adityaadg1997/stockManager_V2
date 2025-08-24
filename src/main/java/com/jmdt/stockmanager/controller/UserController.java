package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.payloads.UserDto;
import com.jmdt.stockmanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    //sign-up new user
    @PostMapping("/register/{userRole}")
    public ResponseEntity<UserDto> signUpNewUser(@RequestBody UserDto userDto, @PathVariable String userRole) {
        UserDto signedUpUser = this.userService.signUp(userDto, userRole);
        return new ResponseEntity<>(signedUpUser, HttpStatus.CREATED);
    }
}
