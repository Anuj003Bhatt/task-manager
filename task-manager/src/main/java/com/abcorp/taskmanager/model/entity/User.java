package com.abcorp.taskmanager.model.entity;

import com.abcorp.taskmanager.converter.OneWayCryptoConverter;
import com.abcorp.taskmanager.converter.TwoWayCryptoConverter;
import com.abcorp.taskmanager.converter.UserStatusConverter;
import com.abcorp.taskmanager.model.base.DtoBridge;
import com.abcorp.taskmanager.model.response.UserDto;
import com.abcorp.taskmanager.type.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseAuditable implements DtoBridge<UserDto> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @UuidGenerator
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    @Convert(converter = TwoWayCryptoConverter.class)
    private String email;

    @Column(name = "password")
    @Convert(converter = OneWayCryptoConverter.class)
    private String password;

    @Column(name = "phone")
    @Convert(converter = TwoWayCryptoConverter.class)
    private String phone;

    @Column(name = "status")
    @Convert(converter = UserStatusConverter.class)
    private UserStatus status = UserStatus.ACTIVE;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    @Override
    public UserDto toDto() {
        return UserDto
                .builder()
                .id(id)
                .phone(phone)
                .email(email)
                .status(status)
                .name(name)
                .build();
    }
}
