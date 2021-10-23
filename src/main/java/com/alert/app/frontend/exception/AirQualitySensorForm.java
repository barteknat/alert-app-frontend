//package com.alert.app.frontend.form;
//
//import com.alert.app.frontend.dto.AirQualitySensorDto;
//import com.alert.app.frontend.service.AlertService;
//import com.alert.app.frontend.view.MainView;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.grid.Grid;
//
//public class AirQualitySensorForm extends FormLayout {
//
//    private MainView mainView;
//    private AlertService alertService;
//    private Grid<AirQualitySensorDto> grid = new Grid<>(AirQualitySensorDto.class);
//    private Button show = new Button("Show air pollution");
//    private String city;
//
//    public AirQualitySensorForm(MainView mainView, AlertService alertService, String city) {
//        this.mainView = mainView;
//        this.alertService = alertService;
//        this.city = city;
//
//
//        show.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        show.addClickListener(event -> show());
//        add(grid, show);
//    }
//
//    private void show() {
//        long stationId = alertService.findByCity(city).getStationApiId();
//        grid.setItems(alertService.getAllSensorsByStationId(stationId));
//        grid.setVisible(true);
//    }
//}
