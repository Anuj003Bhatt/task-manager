package com.abcorp.taskmanager.model.response;

import com.abcorp.taskmanager.type.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private UserStatus status;
}
