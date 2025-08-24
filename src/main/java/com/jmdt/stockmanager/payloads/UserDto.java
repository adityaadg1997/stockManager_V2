package com.jmdt.stockmanager.payloads;

import com.jmdt.stockmanager.models.Role;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<Role> roles;
}
