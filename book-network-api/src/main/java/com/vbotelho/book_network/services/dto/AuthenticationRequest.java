package com.vbotelho.book_network.services.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest (
        @Email(message = "Email is not well formatted")
        @NotBlank(message = "Email is mandatory")
        String email,
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password should be 8 characters long minimum")
        String password){}
