package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.exception.ResourceNotFoundException;
import com.jmdt.stockmanager.models.MyUser;
import com.jmdt.stockmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //load data from DB
        MyUser user = this.userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("MyUser", "username", username));
        return user;
    }
}
