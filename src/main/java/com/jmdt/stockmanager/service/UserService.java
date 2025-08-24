package com.jmdt.stockmanager.service;


import com.jmdt.stockmanager.payloads.UserDto;

public interface UserService {

    //signUp
    UserDto signUp(UserDto userDto, String userRole);

}
