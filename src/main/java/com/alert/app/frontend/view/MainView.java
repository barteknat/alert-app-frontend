package com.alert.app.frontend.view;

import com.alert.app.frontend.dto.AirQualitySensorDto;
import com.alert.app.frontend.dto.AirQualityStationDto;
import com.alert.app.frontend.dto.UserDto;
import com.alert.app.frontend.dto.WeatherStationDto;
import com.alert.app.frontend.service.AlertService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Route
public class MainView extends VerticalLayout {

    private AlertService alertService;
    private Grid<AirQualityStationDto> gridStation = new Grid<>(AirQualityStationDto.class);
    private Grid<AirQualitySensorDto> gridSensor = new Grid<>(AirQualitySensorDto.class);
    private Grid<WeatherStationDto> gridWeather = new Grid<>(WeatherStationDto.class);
//    private ComboBox<AirQualityStationDto> combo = new ComboBox<>();
    private TextField textField = new TextField();
    private Button show = new Button("Details");
    private TextField email = new TextField("Set e-mail");
    private Button subscribe = new Button("Subscribe");


    public MainView(AlertService alertService) {
        this.alertService = alertService;
//        List<AirQualityStationDto> airQualityStationDtoList = alertFacade.getAllStations();
        gridStation.setColumns("city", "stationApiId");
        gridSensor.setColumns("name", "code", "value", "date");
        gridWeather.setColumns("temperature", "windSpeed", "humidity", "rainAmount", "pressure", "date", "time");
//        grid.setVisible(false);
        textField.setPlaceholder("Filter by city");
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> upadte());
//        gridStation.setSizeFull();
//        gridSensor.setSizeFull();
        show.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        show.addClickListener(event -> show());
        email.addValueChangeListener(event -> createUser(email.getValue()));
        subscribe.addClickListener(event -> createSubscribe(getUserId(email.getValue()), textField.getValue()));
        HorizontalLayout sub = new HorizontalLayout(textField, email, subscribe);
        add(sub, gridStation, show, gridSensor, gridWeather);
        refresh();

//        List<AirQualityStationDto> airQualityStationDtoList = alertFacade.getAllStations();
//        airQualityStationDtoList.sort(Comparator.comparing(AirQualityStationDto::getCity));
//        combo.setItems(airQualityStationDtoList);
//        combo.setItemLabelGenerator(AirQualityStationDto::getCity);
//        grid.setColumns("city");
//        grid.setItems(alertFacade.getAllStations());
//        add(combo);
//        setSizeFull();
//        refresh();

//        FOR LAST FORM OF APP
//        alertService.setAllWeatherStations();
//        alertService.setAllAirQualityStations();
//        System.out.println(alertFacade.getAllSensorsByStationId(605));
    }

    private void show() {
        alertService.setAllWeatherStations();
        alertService.setAllAirQualityStations();
        gridSensor.setItems(alertService.getAllAirQualitySensorsByStationId(alertService.findByCity(textField.getValue()).getStationApiId()));
        gridWeather.setItems(alertService.getWeatherStationByCity(textField.getValue()));
    }

    private void createUser(String email) {
        alertService.createUser(UserDto.builder().email(email).build());
    }

    private long getUserId(String email) {
        return alertService.getUserByEmail(email).getId();
    }

    private void createSubscribe(long userId, String city) {
        alertService.createSubscribe(userId, city);
    }

    private void upadte() {
        gridStation.setItems(alertService.findByCity(textField.getValue()));
    }

    private void refresh() {
        gridStation.setItems(alertService.getAllAirQualityStations());
//        System.out.println(alertFacade.getAllStations().get(0).getCity());
    }
}
