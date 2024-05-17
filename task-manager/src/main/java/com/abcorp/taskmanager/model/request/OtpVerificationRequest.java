package com.abcorp.taskmanager.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerificationRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Not a valid email")
    String email;
    @NotBlank(message = "OTP cannot be blank")
    String otp;
}
