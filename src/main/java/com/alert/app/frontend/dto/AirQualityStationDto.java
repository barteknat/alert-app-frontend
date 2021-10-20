package com.alert.app.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirQualityStationDto {

    private long id;
    private long stationApiId;
//    private String name;
//    private String street;
    private String city;
}
