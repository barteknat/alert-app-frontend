package com.alert.app.frontend.service;

import com.alert.app.frontend.client.AlertClient;
import com.alert.app.frontend.dto.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
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

    public void createUser(UserDto userDto) {
        alertClient.createUser(userDto);
    }

//    public List<UserDto> getAllUsers() {
//        return alertClient.getAllUsers();
//    }

    public UserDto getUserByEmail(String email) {
        return alertClient.getUserByEmail(email);
    }

//    public UserDto getUserByUsername(String username) {
//        return alertClient.getUserByUsername(username);
//    }

    public void updateUser(UserDto userDto) {
        alertClient.updateUser(userDto);
    }

    public void logInUser(String email, String password) {
        alertClient.logInUser(email, password);
    }

    public void logOutUser(String email) {
        alertClient.logOutUser(email);
    }

    public void deleteUser(long userId) {
        alertClient.deleteUser(userId);
    }

    public List<SubscribeDto> getAllSubscribesByUserId(long userId) {
        return alertClient.getAllSubscribesByUserId(userId);
    }

    public SubscribeDto getSubscribeByUserIdAndCity(long userId, String city) {
        return alertClient.getSubscribeByUserIdAndCity(userId, city);
    }

    public void createSubscribe(long userId, String city) {
        alertClient.createSubscribe(userId, city);
    }

    public void deleteSubscribe(long subscribeId) {
        alertClient.deleteSubscribe(subscribeId);
    }
}
