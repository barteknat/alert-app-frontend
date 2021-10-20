package com.alert.app.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirQualityIndexDto {

    private long id;
    private long stationApiId;
    private LocalDateTime date;
    private long level;
    private String levelName;
}
