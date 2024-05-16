package com.abcorp.taskmanager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddUserDto {
    @NotBlank(message = "Name for a user cannot be blank")
    @Schema(example = "John Doe")
    private String name;
    @NotBlank(message = "Password for a user cannot be blank")
    private String password;
    @Email(message = "Email ID provided is not valid")
    @NotBlank
    @Schema(example = "abc@xyz.com")
    private String email;
    @NotBlank(message = "User needs to provide a valid mobile number.")
    @Pattern(
            regexp = "((\\+*)((0[ -]*)*|((91 )*))((\\d{12})+|(\\d{10})+))|\\d{5}([- ]*)\\d{6}",
            message = "Provided mobile number is not valid"
    )
    @Schema(example = "+911234567890")
    private String phone;
}
