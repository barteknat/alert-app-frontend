package com.alert.app.frontend.view;

import com.alert.app.frontend.dto.*;
import com.alert.app.frontend.service.AlertService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SpringComponent
@PreserveOnRefresh
@UIScope
@Route
@Theme(value = Lumo.class)
@StyleSheet("../frontend/styles/style.css")
public class MainView extends VerticalLayout {

    private final AlertService alertService;
    private UserDto userDto = null;
    private final MenuBar menu = new MenuBar();
    private final MenuBar menuLoggedIn = new MenuBar();
    private final H1 header = new H1("AIR QUALITY ALERT APP");
    private final Select<AirQualityStationDto> citySelect = new Select<>();
    private final Grid<AirQualitySensorDto> pollutionGrid = new Grid<>(AirQualitySensorDto.class);
    private final Label pollutionTitle = new Label();
    private final Label weatherTitle = new Label();
    private final Label temperature = new Label();
    private final Label windSpeed = new Label();
    private final Label humidity = new Label();
    private final Label pressure = new Label();
    private final Button details = new Button("DETAILS");
    private final Button subscribe = new Button("SUBSCRIBE");
    private final Button hideDetails = new Button("HIDE DETAILS");
    private final Button signUp = new Button("SIGN UP");
    private final Button logIn = new Button("LOG IN");
    private final Button logOut = new Button("LOG OUT");
    private final Button info = new Button("INFO");
    private final Button account = new Button("ACCOUNT");
    private final Dialog dialog = new Dialog();
    private final Dialog infoDialog = new Dialog();
    private final HorizontalLayout selectHorizontalLayout = new HorizontalLayout(citySelect, details, subscribe, hideDetails);
    private final VerticalLayout detailsVerticalLayout = new VerticalLayout(pollutionTitle, pollutionGrid, weatherTitle, temperature, windSpeed, humidity, pressure);

    public MainView(AlertService alertService) {
        this.alertService = alertService;
        this.addClassNames("body");
        styleComponents();
        loadCities();
        setPollutionGrid();
        menu.addItem(signUp);
        menu.addItem(logIn);
        menu.addItem(info);
        menuLoggedIn.addItem(logOut);
        menuLoggedIn.addItem(account);
        hideDetails.setVisible(false);

        info.addClickListener(event -> {
            showInfo();
        });

        signUp.addClickListener(event -> {
            signUp.getUI().ifPresent(ui -> ui.navigate("signup"));
        });

        logIn.addClickListener(event -> {
            logIn.getUI().ifPresent(ui -> ui.navigate("login"));
        });

        account.addClickListener(event -> {
            account.getUI().ifPresent(ui -> ui.navigate("account"));
        });

        logOut.addClickListener(event -> {
            if (logOut()) {
                removeAll();
                add(new VerticalLayout(menu, header, selectHorizontalLayout, detailsVerticalLayout));
                subscribe.setVisible(false);
            }
        });

        details.addClickListener(event -> {
            if (citySelect.getValue() == null) {
                showDialog("YOU HAVE TO CHOOSE CITY FIRST");
                return;
            }
            setAirPollutionAndWeatherDetails();
            hideDetails.setVisible(true);
            detailsVerticalLayout.setVisible(true);
        });

        subscribe.addClickListener(event -> {
            if (citySelect.getValue() == null) {
                showDialog("YOU HAVE TO CHOOSE CITY FIRST");
                return;
            }
            subscribe();
        });

        hideDetails.addClickListener(event -> {
            detailsVerticalLayout.setVisible(false);
            hideDetails.setVisible(false);
        });

        VerticalLayout mainLayout = new VerticalLayout(menu, header, selectHorizontalLayout, detailsVerticalLayout);
        add(mainLayout);
    }

    public void setLoggedInLayer() {
        removeAll();
        add(new VerticalLayout(menuLoggedIn, header, selectHorizontalLayout, detailsVerticalLayout));
        subscribe.setVisible(true);
    }

    public void setLoggedOutLayer() {
        removeAll();
        add(new VerticalLayout(menu, header, selectHorizontalLayout, detailsVerticalLayout));
        subscribe.setVisible(false);
    }

    public void showDialog(String text) {
        dialog.removeAll();
        dialog.add(new Text(text));
        dialog.open();
    }

    private void showInfo() {
        infoDialog.removeAll();
        infoDialog.add(new Text("ALERT APP ALLOWS YOU TO CHECKING ACTUAL AIR " +
                "POLLUTION AND WEATHER DETAILS IN CHOSEN CITY. YOU CAN ALSO " +
                "SUBSCRIBE TO BE ALERTED BY E-MAIL ABOUT BAD AIR CONDITION IN " +
                "THE CITY, BUT TO DO THAT, YOU HAVE TO SIGN UP AND LOG IN FIRST."
        ));
        infoDialog.open();
    }

    private boolean logOut() {
        if (userDto == null) return false;
        alertService.logOutUser(userDto.getEmail());
        userDto = null;
        showDialog("YOU ARE LOGGED OUT");
        return true;
    }

    private void setAirPollutionAndWeatherDetails() {
        WeatherStationDto weatherStationDto = alertService.getWeatherStationByCity(citySelect.getValue().getCity());
        List<AirQualitySensorDto> airQualitySensorDtos = alertService.getAllAirQualitySensorsByStationId(alertService.findByCity(citySelect.getValue().getCity()).getStationApiId());
        weatherTitle.setText("WEATHER IN " + weatherStationDto.getCity() + " ON " + weatherStationDto.getDate() + "T" + weatherStationDto.getTime() + ":00");
        temperature.setText("TEMPERATURE: " + weatherStationDto.getTemperature() + " °C");
        windSpeed.setText("WIND SPEED: " + weatherStationDto.getWindSpeed() + " km/h");
        humidity.setText("HUMIDITY: " + weatherStationDto.getHumidity() + " %");
        pressure.setText("PRESSURE: " + weatherStationDto.getPressure() + " hPa");
        pollutionTitle.setText("AIR POLLUTION IN " + weatherStationDto.getCity() + " ON " + airQualitySensorDtos.get(0).getDate());
        pollutionGrid.setItems(airQualitySensorDtos);
    }

    private void subscribe() {
        if (userDto == null) return;
        SubscribeDto subscribeDto = alertService.getSubscribeByUserIdAndCity(userDto.getId(), citySelect.getValue().getCity());
        if (subscribeDto != null) {
            showDialog("YOU ARE ALREADY SUBSCRIBING THIS CITY");
            return;
        }
        alertService.createSubscribe(userDto.getId(), citySelect.getValue().getCity());
        showDialog("YOU ARE SUBSCRIBING >>" + citySelect.getValue().getCity() + "<< " +
                "YOU WILL RECEIVE ALERT WHEN AIR CONDITION WILL BE BAD");
    }

    private void loadCities() {
//        alertService.setAllWeatherStations();
//        alertService.setAllAirQualityStations();
        List<AirQualityStationDto> stationDtoList = alertService.getAllAirQualityStations();
        citySelect.setItemLabelGenerator(AirQualityStationDto::getCity);
        citySelect.setItems(stationDtoList);
    }

    private void setPollutionGrid() {
        pollutionGrid.setColumns("name", "code", "value");
        pollutionGrid.getColumnByKey("name").setHeader("POLLUTION NAME");
        pollutionGrid.getColumnByKey("code").setHeader("PATTERN");
        pollutionGrid.getColumnByKey("value").setHeader("VALUE [µg/m³]");
    }

    private void styleComponents() {
        header.getStyle()
                .set("border", "2px solid black")
                .set("color", "black")
                .set("margin", "-22px 0px 0px 0px");
        signUp.getStyle()
                .set("color", "black");
        logIn.getStyle()
                .set("color", "black");
        logOut.getStyle()
                .set("color", "black");
        info.getStyle()
                .set("color", "black");
        account.getStyle()
                .set("color", "black");
        citySelect.getStyle()
                .set("border", "1px solid black");
        citySelect.setHelperText("SELECT CITY");
        subscribe.getStyle()
                .set("border", "1px solid black")
                .set("color", "black")
                .set("background", "darkgrey");
        subscribe.setWidth("150x");
        subscribe.setHeight("45px");
        subscribe.setIcon(new Icon(VaadinIcon.ALARM));
        subscribe.setVisible(false);
        details.getStyle()
                .set("border", "1px solid black")
                .set("color", "black")
                .set("background", "darkgrey");
        details.setWidth("128px");
        details.setHeight("45px");
        details.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_DOWN_O));
        hideDetails.getStyle()
                .set("border", "1px solid black")
                .set("color", "black")
                .set("background", "darkgrey");
        hideDetails.setWidth("170px");
        hideDetails.setHeight("45px");
        hideDetails.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_UP_O));
        pollutionTitle.getStyle()
                .set("border", "3px solid black")
                .set("background", "white")
                .set("color", "black");
        pollutionGrid.getStyle()
                .set("border", "1px solid black")
                .set("color", "black");
        pollutionGrid.setWidth("450px");
        pollutionGrid.setHeight("180px");
        weatherTitle.getStyle()
                .set("border", "3px solid black")
                .set("background", "white")
                .set("color", "black");
        temperature.getStyle()
                .set("border", "1px solid black")
                .set("background", "white")
                .set("color", "black");
        windSpeed.getStyle()
                .set("border", "1px solid black")
                .set("background", "white")
                .set("color", "black");
        humidity.getStyle()
                .set("border", "1px solid black")
                .set("background", "white")
                .set("color", "black");
        pressure.getStyle()
                .set("border", "1px solid black")
                .set("background", "white")
                .set("color", "black");
        infoDialog.setWidth("480px");
        detailsVerticalLayout.setVisible(false);
        selectHorizontalLayout.getStyle()
                .set("margin", "30px 0px 0px 580px");
        setHorizontalComponentAlignment(Alignment.CENTER, header);
        setHorizontalComponentAlignment(Alignment.CENTER, pollutionTitle);
        setHorizontalComponentAlignment(Alignment.CENTER, pollutionGrid);
        setHorizontalComponentAlignment(Alignment.CENTER, weatherTitle);
        setHorizontalComponentAlignment(Alignment.CENTER, temperature);
        setHorizontalComponentAlignment(Alignment.CENTER, windSpeed);
        setHorizontalComponentAlignment(Alignment.CENTER, humidity);
        setHorizontalComponentAlignment(Alignment.CENTER, pressure);
    }
}
