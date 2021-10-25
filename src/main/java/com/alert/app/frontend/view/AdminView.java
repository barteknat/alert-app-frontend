package com.alert.app.frontend.view;

import com.alert.app.frontend.dto.*;
import com.alert.app.frontend.service.AlertService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

@PreserveOnRefresh
@UIScope
@Route(value = "admin")
@StyleSheet("../frontend/styles/style.css")
public class AdminView extends VerticalLayout {

    private final MainView mainView;
    private final AlertService alertService;
    private final Label allUsers = new Label("ALL USERS");
    private final Label allSubscribes = new Label("ALL SUBSCRIBES");
    private final Label allStatistics = new Label("ALL STATISTICS");
    private final Label allSensors = new Label("ALL AIR QUALITY SENSORS");
    private final Label allIndexes = new Label("ALL AIR QUALITY INDEXES");
    private final Label allWeatherStations = new Label("ALL WEATHER STATIONS");
    private final Button goToMainView = new Button("MAIN VIEW");
    private final Grid<UserDto> allUsersGrid = new Grid<>(UserDto.class);
    private final Grid<SubscribeDto> allSubscribesGrid = new Grid<>(SubscribeDto.class);
    private final Grid<StatisticsDto> allStatisticsGrid = new Grid<>(StatisticsDto.class);
    private final Grid<AirQualitySensorDto> allSensorsGrid = new Grid<>(AirQualitySensorDto.class);
    private final Grid<AirQualityIndexDto> allIndexesGrid = new Grid<>(AirQualityIndexDto.class);
    private final Grid<WeatherStationDto> allWeatherStationsGrid = new Grid<>(WeatherStationDto.class);

    public AdminView(MainView mainView, AlertService alertService) {
        this.mainView = mainView;
        this.alertService = alertService;
        this.addClassNames("body");
        styleComponents();
        setGrids();
        setGridsData();

        goToMainView.addClickListener(event -> {
            goToMainView.getUI().ifPresent(ui -> ui.navigate(""));
        });

        VerticalLayout mainVerticalLayout = new VerticalLayout(
                allUsers, allUsersGrid, allSubscribes, allSubscribesGrid, allStatistics,
                allStatisticsGrid, allWeatherStations, allWeatherStationsGrid, allSensors,
                allSensorsGrid, allIndexes, allIndexesGrid, goToMainView);
        add(mainVerticalLayout);
    }

    private void setGrids() {
        allUsersGrid.setColumns("id", "username", "email", "password", "logStatus", "subStatus", "created");
        allSubscribesGrid.setColumns("id", "userEmail", "city");
        allStatisticsGrid.setColumns("id", "status", "date", "remarks");
        allWeatherStationsGrid.setColumns("id", "city", "date", "time", "temperature");
        allSensorsGrid.setColumns("id", "stationApiId", "name", "code", "date", "value");
        allIndexesGrid.setColumns("id", "stationApiId", "date", "level", "levelName");
    }

    private void setGridsData() {
        allUsersGrid.setItems(alertService.getAllUsers());
        allSubscribesGrid.setItems(alertService.getAllSubscribes());
        allStatisticsGrid.setItems(alertService.getAllStatistics());
        allWeatherStationsGrid.setItems(alertService.getAllWeatherStations());
        allSensorsGrid.setItems(alertService.getAllAirQualitySensors());
        allIndexesGrid.setItems(alertService.getAllAirQualityIndexes());
    }

    private void styleComponents() {
        allUsers.getStyle()
                .set("background", "grey")
                .set("border", "1px solid black");
        allUsers.setWidth("300px");
        allSubscribes.getStyle()
                .set("background", "grey")
                .set("border", "1px solid black");
        allSubscribes.setWidth("300px");
        allStatistics.getStyle()
                .set("background", "grey")
                .set("border", "1px solid black");
        allStatistics.setWidth("300px");
        allWeatherStations.getStyle()
                .set("background", "grey")
                .set("border", "1px solid black");
        allWeatherStations.setWidth("300px");
        allSensors.getStyle()
                .set("background", "grey")
                .set("border", "1px solid black");
        allSensors.setWidth("300px");
        allIndexes.getStyle()
                .set("background", "grey")
                .set("border", "1px solid black");
        allIndexes.setWidth("300px");
        allUsersGrid.getStyle()
                .set("border", "1px solid black")
                .set("margin", "-3px 0px 0px 0px")
                .set("color", "black");
        allUsersGrid.setWidth("1200px");
        allUsersGrid.setHeight("200px");
        allSubscribesGrid.getStyle()
                .set("border", "1px solid black")
                .set("margin", "-3px 0px 0px 0px")
                .set("color", "black");
        allSubscribesGrid.setWidth("1200px");
        allSubscribesGrid.setHeight("200px");
        allStatisticsGrid.getStyle()
                .set("border", "1px solid black")
                .set("margin", "-3px 0px 0px 0px")
                .set("color", "black");
        allStatisticsGrid.setWidth("1200px");
        allStatisticsGrid.setHeight("200px");
        allWeatherStationsGrid.getStyle()
                .set("border", "1px solid black")
                .set("margin", "-3px 0px 0px 0px")
                .set("color", "black");
        allWeatherStationsGrid.setWidth("1200px");
        allWeatherStationsGrid.setHeight("200px");
        allSensorsGrid.getStyle()
                .set("border", "1px solid black")
                .set("margin", "-3px 0px 0px 0px")
                .set("color", "black");
        allSensorsGrid.setWidth("1200px");
        allSensorsGrid.setHeight("200px");
        allIndexesGrid.getStyle()
                .set("border", "1px solid black")
                .set("margin", "-3px 0px 0px 0px")
                .set("color", "black");
        allIndexesGrid.setWidth("1200px");
        allIndexesGrid.setHeight("200px");
        goToMainView.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");
        goToMainView.setWidth("200px");
        goToMainView.setHeight("45px");
        setHorizontalComponentAlignment(Alignment.CENTER, allUsers);
        setHorizontalComponentAlignment(Alignment.CENTER, allUsersGrid);
        setHorizontalComponentAlignment(Alignment.CENTER, allSubscribes);
        setHorizontalComponentAlignment(Alignment.CENTER, allSubscribesGrid);
        setHorizontalComponentAlignment(Alignment.CENTER, allStatistics);
        setHorizontalComponentAlignment(Alignment.CENTER, allStatisticsGrid);
        setHorizontalComponentAlignment(Alignment.CENTER, allWeatherStations);
        setHorizontalComponentAlignment(Alignment.CENTER, allWeatherStationsGrid);
        setHorizontalComponentAlignment(Alignment.CENTER, allSensors);
        setHorizontalComponentAlignment(Alignment.CENTER, allSensorsGrid);
        setHorizontalComponentAlignment(Alignment.CENTER, allIndexes);
        setHorizontalComponentAlignment(Alignment.CENTER, allIndexesGrid);
        setHorizontalComponentAlignment(Alignment.CENTER, goToMainView);
    }
}
