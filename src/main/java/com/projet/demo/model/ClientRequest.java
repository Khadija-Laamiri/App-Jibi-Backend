package com.projet.demo.model;

import com.projet.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data

public class ClientRequest {
    private Long agentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String newPassword;
    @ToString.Exclude
    private Role role;
}
