package com.akash.personalTracker.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Integer userId;
    private String email;
}