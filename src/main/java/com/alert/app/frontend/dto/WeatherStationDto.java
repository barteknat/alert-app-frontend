package com.alert.app.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherStationDto {

    private long id;
    private long stationId;
    private String city;
    private LocalDate date;
    private long time;
    private double temperature;
    private double windSpeed;
    private double windDirection;
    private double humidity;
    private double rainAmount;
    private double pressure;
}
