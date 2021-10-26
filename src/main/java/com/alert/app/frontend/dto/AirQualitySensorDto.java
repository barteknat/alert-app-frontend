package com.alert.app.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirQualitySensorDto {

    private long id;
    private long sensorApiId;
    private long stationApiId;
    private String name;
    private String code;
    private LocalDateTime date;
    private double value;
}
