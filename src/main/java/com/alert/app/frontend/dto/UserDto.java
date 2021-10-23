package com.alert.app.frontend.dto;

import com.alert.app.frontend.status.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private long id;
    private String username;
    private String email;
    private String password;
    private UserStatus logStatus;
    private UserStatus subStatus;
    private LocalDateTime created;
}
