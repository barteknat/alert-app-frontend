package com.alert.app.frontend.dto;

import com.alert.app.frontend.status.StatisticsStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDto {

    private long id;
    private StatisticsStatus status;
    private LocalDateTime date;
    private String remarks;
}
