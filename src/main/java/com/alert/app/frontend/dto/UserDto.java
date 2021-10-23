package com.alert.app.frontend.dto;

import com.alert.app.frontend.status.UserStatus;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SpringComponent
public class UserDto {

    private long id;
    private String username;
    private String email;
    private String password;
    private UserStatus logStatus;
    private UserStatus subStatus;
    private LocalDateTime created;
}
