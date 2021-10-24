package com.alert.app.frontend.client;

import com.alert.app.frontend.config.AlertConfig;
import com.alert.app.frontend.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        try {
            restTemplate.getForObject(createUriForSetAllWeatherStations(), Void.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void setAllAirQualityStations() {
        try {
            restTemplate.getForObject(createUriForSetAllAirQualityStations(), Void.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        }
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

    public void createUser(UserDto userDto) {
        try {
            restTemplate.postForObject(createUriForCreateUser(), userDto, UserDto.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            new UserDto();
        }
    }

    public List<UserDto> getAllUsers() {
        try {
            return Arrays.asList(Optional.ofNullable(restTemplate.getForObject(createUriForGetAllUsers(), UserDto[].class))
                    .orElse(new UserDto[0]));
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
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

    public UserDto getUserByUsername(String username) {
        try {
            return restTemplate.getForObject(createUriForGetUserByUsername(username), UserDto.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new UserDto();
        }
    }

    public void updateUser(UserDto userDto) {
        try {
            restTemplate.put(createUriForUpdateUser(), userDto);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void logInUser(String email, String password) {
        try {
            restTemplate.put(createUriForLogInUser(email, password), null);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void logOutUser(String email) {
        try {
            restTemplate.put(createUriForLogOutUser(email), null);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void deleteUser(long userId) {
        try {
            restTemplate.delete(createUriForDeleteUser(userId));
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<SubscribeDto> getAllSubscribesByUserId(long userId) {
        try {
            return Arrays.asList(Optional.ofNullable(restTemplate.getForObject(createUriForGetAllSubscribesByUserId(userId), SubscribeDto[].class))
                    .orElse(new SubscribeDto[0]));
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public SubscribeDto getSubscribeByUserIdAndCity(long userId, String city) {
        try {
            return restTemplate.getForObject(createUriForGetSubscribeByUserIdAndCity(userId, city), SubscribeDto.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new SubscribeDto();
        }
    }

    public void createSubscribe(long userId, String city) {
        try {
            restTemplate.postForObject(createUriForCreateSubscribe(userId, city), null, SubscribeDto.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void deleteSubscribe(long subscribeId) {
        try {
            restTemplate.delete(createUriForDeleteSubscribe(subscribeId));
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
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

    private URI createUriForGetAllUsers() {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/user/all")
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

    private URI createUriForGetUserByUsername(String username) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/user/name")
                .queryParam("username", username)
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

    private URI createUriForUpdateUser() {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/user")
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForLogInUser(String email, String password) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/user/logIn")
                .queryParam("email", email)
                .queryParam("password", password)
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForLogOutUser(String email) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/user/logOut")
                .queryParam("email", email)
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForDeleteUser(long userId) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/user/" + userId)
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForGetAllSubscribesByUserId(long userId) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/subscribe/allById")
                .queryParam("userId", userId)
                .build()
                .encode()
                .toUri();
    }

    private URI createUriForGetSubscribeByUserIdAndCity(long userId, String city) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/subscribe")
                .queryParam("userId", userId)
                .queryParam("city", city)
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

    private URI createUriForDeleteSubscribe(long subscribeId) {
        return UriComponentsBuilder.fromHttpUrl(alertConfig.getAlertBackendApiEndpoint() + "/subscribe/" + subscribeId)
                .build()
                .encode()
                .toUri();
    }
}
