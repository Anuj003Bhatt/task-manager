package com.abcorp.taskmanager.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Not a valid email")
    String email;
    @NotNull(message = "Password cannot be changed without OTP verification")
    UUID verificationId;
    @NotBlank(message = "New password cannot be null while changing old password")
    String newPassword;
}
