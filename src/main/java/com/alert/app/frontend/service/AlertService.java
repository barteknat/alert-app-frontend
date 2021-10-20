package com.alert.app.frontend.service;

import com.alert.app.frontend.client.AlertClient;
import com.alert.app.frontend.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AlertService {

    private final AlertClient alertClient;

    public void setAllWeatherStations() {
        alertClient.setAllWeatherStations();
    }

    public void setAllAirQualityStations() {
        alertClient.setAllAirQualityStations();
    }

    public List<AirQualityStationDto> getAllAirQualityStations() {
        return alertClient.getAllAirQualityStations();
    }

    public List<AirQualitySensorDto> getAllAirQualitySensorsByStationId(long stationId) {
        return alertClient.getAllAirQualitySensorsByStationId(stationId);
    }

    public WeatherStationDto getWeatherStationByCity(String city) {
        return alertClient.getWeatherStationByCity(city);
    }

    public AirQualityStationDto findByCity(String city) {
        return getAllAirQualityStations().stream().filter(station -> station.getCity().contains(city)).collect(Collectors.toList()).get(0);
    }

    public UserDto createUser(UserDto userDto) {
        return alertClient.createUser(userDto);
    }

    public UserDto getUserByEmail(String email) {
        return alertClient.getUserByEmail(email);
    }

    public SubscribeDto createSubscribe(long userId, String city) {
        return alertClient.createSubscribe(userId, city);
    }
}
