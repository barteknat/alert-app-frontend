package com.alert.app.frontend.client;

import com.alert.app.frontend.AlertFrontApplication;
import com.alert.app.frontend.config.AlertConfig;
import com.alert.app.frontend.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertClient {

    private final RestTemplate restTemplate;
    private final AlertConfig alertConfig;


    public void setAllWeatherStations() {
        restTemplate.getForObject(createUriForSetAllWeatherStations(), Void.class);
    }

    public void setAllAirQualityStations() {
        restTemplate.getForObject(createUriForSetAllAirQualityStations(), Void.class);
    }

    public List<AirQualityStationDto> getAllAirQualityStations() {
        try {
            return Arrays.asList(Optional.ofNullable(restTemplate.getForObject(createUriForGetAllAirQualityStations(), AirQualityStationDto[].class))
                    .orElse(new AirQualityStationDto[0]));
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<AirQualitySensorDto> getAllAirQualitySensorsByStationId(long stationId) {
        try {
            return Arrays.asList(Optional.ofNullable(restTemplate.getForObject(createUriForGetAllAirQualitySensors(stationId), AirQualitySensorDto[].class))
                    .orElse(new AirQualitySensorDto[0]));
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public WeatherStationDto getWeatherStationByCity(String city) {
        try {
            return restTemplate.getForObject(createUriForGetWeatherStation(city), WeatherStationDto.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new WeatherStationDto();
        }
    }

    public UserDto createUser(UserDto userDto) {
        try {
            return restTemplate.postForObject(createUriForCreateUser(), userDto, UserDto.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new UserDto();
        }
    }

    public UserDto getUserByEmail(String email) {
        try {
            return restTemplate.getForObject(createUriForGetUserByEmail(email), UserDto.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new UserDto();
        }
    }

    public SubscribeDto createSubscribe(long userId, String city) {
        try {
            return restTemplate.postForObject(createUriForCreateSubscribe(userId, city), null, SubscribeDto.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new SubscribeDto();
        }
    }

    private URI createUriForSetAllWeatherStations() {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/imgw/stations")
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForSetAllAirQualityStations() {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/gios/stations")
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForGetAllAirQualityStations() {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/station")
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForGetAllAirQualitySensors(long stationId) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/sensor")
                .queryParam("stationId", stationId)
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForGetWeatherStation(String city) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/weatherStation")
                .queryParam("city", city)
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForCreateUser() {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/user")
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForGetUserByEmail(String email) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/user")
                .queryParam("email", email)
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForCreateSubscribe(long userId, String city) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/subscribe")
                .queryParam("userId", userId)
                .queryParam("city", city)
                .build()
                .encode()
                .toUri();
    }
}
