package com.alert.app.frontend.view;

import com.alert.app.frontend.dto.*;
import com.alert.app.frontend.service.AlertService;
import com.alert.app.frontend.status.UserStatus;
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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.List;

@Route
@Theme(value = Lumo.class)
@StyleSheet("../frontend/styles/style.css")
public class MainView extends Div {

    private final AlertService alertService;

    private final H1 header1 = new H1("AIR QUALITY ALERT APP");
    private final H4 header2 = new H4("CHECK ACTUAL AIR POLLUTION AND WEATHER DETAILS >>>>");
    private final H4 header3 = new H4("SUBSCRIBE TO BE ALERTED ON AIR CONDITION BY MAIL >>>>");
    private final Select<AirQualityStationDto> selectCity = new Select<>();
    private final Label labelGridTitle = new Label();
    private final Grid<AirQualitySensorDto> gridAirQualitySensor = new Grid<>(AirQualitySensorDto.class);
    private final Label labelWeatherTitle = new Label();
    private final Label labelTemperature = new Label();
    private final Label labelWindSpeed = new Label();
    private final Label labelHumidity = new Label();
    private final Label labelPressure = new Label();
    private final Button buttonDetails = new Button("DETAILS");
    private final Button buttonSubscribe = new Button("SUBSCRIBE");
    private final Button buttonSignUp = new Button("SIGN UP");
    private final Button buttonLogIn = new Button("LOG IN");
    private final Button buttonLogOut = new Button("LOG OUT");
    private final Button buttonInfo = new Button("INFO");
    private final Button buttonUpdateAccount = new Button("UPDATE ACCOUNT");

    private final MenuBar menuBar1 = new MenuBar();
    private final MenuBar menuBar2 = new MenuBar();
    private final TextField textFieldUsernameSignUp = new TextField();
    private final TextField textFieldEmailSignUp = new TextField();
    private final PasswordField passwordFieldSignUp = new PasswordField();
    private final Button buttonCommitSignUp = new Button("SIGN UP");
    private final TextField textFieldEmailLogIn = new TextField();
    private final PasswordField passwordFieldLogIn = new PasswordField();
    private final Button buttonCommitLogIn = new Button("LOG IN");
    private final TextField textFieldGiveEmailForSubscribe = new TextField();
    private final Button buttonSubscribeCommit = new Button("COMMIT SUBSCRIPTION");
    private final Button buttonDeleteSubscribe = new Button("DELETE SUBSCRIPTION");
    private final Dialog dialog = new Dialog();
    private final Dialog dialogInfo = new Dialog();

    public MainView(AlertService alertService) {
        this.alertService = alertService;
        this.addClassNames("body");
        styleComponents();
        loadSelectData();
        setGridColumns();

        menuBar1.addItem(buttonSignUp);
        menuBar1.addItem(buttonLogIn);
        menuBar1.addItem(buttonInfo);
        menuBar2.addItem(buttonLogOut);
        menuBar2.addItem(buttonUpdateAccount);


        VerticalLayout verticalLayoutSignUp = new VerticalLayout(textFieldUsernameSignUp, textFieldEmailSignUp, passwordFieldSignUp, buttonCommitSignUp);
        verticalLayoutSignUp.getStyle()
                .set("color", "black")
                .set("margin", "100px 0px 0px 530px");

        VerticalLayout verticalLayoutLogIn = new VerticalLayout(textFieldEmailLogIn, passwordFieldLogIn, buttonCommitLogIn);
        verticalLayoutLogIn.getStyle()
                .set("color", "black")
                .set("margin", "100px 0px 0px 530px");

        HorizontalLayout horizontalLayoutSubscribeCommit = new HorizontalLayout(textFieldGiveEmailForSubscribe, buttonSubscribeCommit, buttonDeleteSubscribe);
        horizontalLayoutSubscribeCommit.setVisible(false);

        HorizontalLayout horizontalLayoutSubscribeButtons = new HorizontalLayout(selectCity, buttonDetails, buttonSubscribe, horizontalLayoutSubscribeCommit);
        horizontalLayoutSubscribeButtons.getStyle()
                .set("margin", "30px 0px 0px 0px");

        VerticalLayout verticalLayoutAirQualityAndTemperatureDetails = new VerticalLayout(labelGridTitle, gridAirQualitySensor, labelWeatherTitle, labelTemperature, labelWindSpeed, labelHumidity, labelPressure);
        verticalLayoutAirQualityAndTemperatureDetails.setVisible(false);

        VerticalLayout mainLayoutBasic = new VerticalLayout(menuBar1, header1, horizontalLayoutSubscribeButtons, verticalLayoutAirQualityAndTemperatureDetails);

        add(mainLayoutBasic);

//
//        VerticalLayout layoutLogIn = new VerticalLayout(menuBar1, header1, horizontalLayoutSubscribeButtons, verticalLayoutSignUp);
//
//        VerticalLayout layoutLoggedIn = new VerticalLayout(menuBar2, header1, header2, header3, horizontalLayoutSubscribeButtons, verticalLayoutSignUp);

        buttonInfo.addClickListener(event -> {
            showInfo();
        });

        buttonSignUp.addClickListener(event -> {
            removeAll();
            VerticalLayout layoutSignUp = new VerticalLayout(menuBar1, header1, verticalLayoutSignUp);
            add(layoutSignUp);
        });

        buttonCommitSignUp.addClickListener(event -> {
            if (signUp()) {
                removeAll();
                add(new VerticalLayout(menuBar1, header1, horizontalLayoutSubscribeButtons, verticalLayoutAirQualityAndTemperatureDetails));
            }
        });

        buttonLogIn.addClickListener(event -> {
            removeAll();
            VerticalLayout layoutLogIn = new VerticalLayout(menuBar1, header1, horizontalLayoutSubscribeButtons, verticalLayoutLogIn);
            add(layoutLogIn);
        });

        buttonCommitLogIn.addClickListener(event -> {
            if (logIn()) {
                removeAll();
                add(new VerticalLayout(menuBar2, header1, horizontalLayoutSubscribeButtons, verticalLayoutAirQualityAndTemperatureDetails));
            }
        });

        buttonDetails.addClickListener(event -> {
            if (selectCity.getValue() == null) {
                showDialog("YOU HAVE TO CHOOSE CITY FIRST");
                return;
            }
            setAirPollutionAndWeatherDetails();
            verticalLayoutAirQualityAndTemperatureDetails.setVisible(true);
        });

        buttonSubscribe.addClickListener(event -> {
            if (selectCity.getValue() == null) {
                showDialog("YOU HAVE TO CHOOSE CITY FIRST");
                return;
            }
            horizontalLayoutSubscribeCommit.setVisible(true);
        });

        buttonSubscribeCommit.addClickListener(event -> {
            if (selectCity.getValue() == null) {
                showDialog("YOU HAVE TO CHOOSE CITY FIRST");
                return;
            }
            if (commitSubscribe()) {
                horizontalLayoutSubscribeCommit.setVisible(false);
            }
        });

        buttonDeleteSubscribe.addClickListener(event -> {
            if (selectCity.getValue() == null) {
                showDialog("YOU HAVE TO CHOOSE CITY FIRST");
                return;
            }
            deleteSubscribe();
            showDialog("SUBSCRIPTION OF " + selectCity.getValue().getCity() + " HAS BEEN DELETED");
        });


//        buttonLogIn.addClickListener(event -> {
//            VerticalLayout mainLayoutLogIn = new VerticalLayout(header1, horizontalLayoutSubscribeButtons, verticalLayoutLogIn);
//            removeAll();
//            add(mainLayoutLogIn);
//            commitLogInButton.addClickListener(event1 -> {
//                if (logIn()) {
//                    removeAll();
//                    add(menuBar2, mainLayout);
//                } else {
////                removeAll();
////                add(menuBar1, mainLayout);
//                }
//            });
//        });
//        refresh();
    }

    private boolean logIn() {
        if (textFieldEmailLogIn.getValue().isEmpty() ||
                passwordFieldLogIn.getValue().isEmpty()) {
            showDialog("FIELD CANNOT BE EMPTY");
            return false;
        }
        UserDto userDto = alertService.getUserByEmail(textFieldEmailLogIn.getValue());
        if (userDto == null) {
            showDialog("INCORRECT USER");
            return false;
        }
        if (userDto.getLogStatus().equals(UserStatus.LOGGED_IN)) {
            showDialog("USER IS ALREADY LOGGED IN");
            return false;
        }
        if (userDto.getPassword().equals(passwordFieldLogIn.getValue())) {
            alertService.logInUser(textFieldEmailLogIn.getValue(), passwordFieldLogIn.getValue());
            showDialog("YOU ARE LOGGED IN");
            return true;
        }
        showDialog("INCORRECT PASSWORD");
        return false;
    }

    private boolean signUp() {
        if (textFieldUsernameSignUp.getValue().isEmpty() ||
                textFieldEmailSignUp.getValue().isEmpty() ||
                passwordFieldSignUp.getValue().isEmpty()) {
            showDialog("FIELD CANNOT BE EMPTY");
            return false;
        }
        if (passwordFieldSignUp.getValue().length() < 3 || passwordFieldSignUp.getValue().length() > 15) {
            showDialog("PASSWORD IS MIN 3 AND MAX 15 CHARACTERS");
            return false;
        }
        UserDto userDto = alertService.getUserByEmail(textFieldEmailSignUp.getValue());
        if (userDto != null) {
            showDialog("USER WITH THIS E-MAIL ALREADY EXISTS IN DATABASE");
            return false;
        }
        alertService.createUser(UserDto.builder()
                .username(textFieldUsernameSignUp.getValue())
                .email(textFieldEmailSignUp.getValue())
                .password(passwordFieldSignUp.getValue())
                .build());
        showDialog("ACCOUNT HAS BEEN CREATED");
        return true;
    }

    private void showInfo() {
        dialogInfo.add(new Text("ALERT APP ALLOWS YOU TO CHECKING ACTUAL AIR " +
                "POLLUTION AND WEATHER DETAILS IN CHOSEN CITY AND SUBSCRIBE TO " +
                "BE ALERTED BY E-MAIL ON AIR CONDITION IN CHOSEN CITY"
        ));
        dialogInfo.open();
    }

    private boolean commitSubscribe() {
        UserDto userDto = alertService.getUserByEmail(textFieldGiveEmailForSubscribe.getValue());
        if (userDto == null) return false;
        SubscribeDto subscribeDto = alertService.getSubscribeByUserIdAndCity(userDto.getId(), selectCity.getValue().getCity());
        if (subscribeDto != null) {
            showDialog("SUBSCRIBE ALREADY EXISTS");
            return false;
        }
        alertService.createSubscribe(userDto.getId(), selectCity.getValue().getCity());
        showDialog("YOU ARE SUBSCRIBING >>" + selectCity.getValue().getCity() + "<< YOU WILL RECEIVE ALERT WHEN AIR CONDITION WILL BE BAD");
        return true;
    }

    private void deleteSubscribe() {

    }


    private void setAirPollutionAndWeatherDetails() {
//        alertService.setAllWeatherStations();
        WeatherStationDto weatherStationDto = alertService.getWeatherStationByCity(selectCity.getValue().getCity());
        List<AirQualitySensorDto> airQualitySensorDtos = alertService.getAllAirQualitySensorsByStationId(alertService.findByCity(selectCity.getValue().getCity()).getStationApiId());
        labelWeatherTitle.setText("WEATHER IN " + weatherStationDto.getCity() + " ON " + weatherStationDto.getDate() + "T" + weatherStationDto.getTime() + ":00");
        labelTemperature.setText("TEMPERATURE: " + weatherStationDto.getTemperature() + " °C");
        labelWindSpeed.setText("WIND SPEED: " + weatherStationDto.getWindSpeed() + " km/h");
        labelHumidity.setText("HUMIDITY: " + weatherStationDto.getHumidity() + " %");
        labelPressure.setText("PRESSURE: " + weatherStationDto.getPressure() + " hPa");
        System.out.println(weatherStationDto.toString());
        labelGridTitle.setText("AIR POLLUTION IN " + weatherStationDto.getCity() + " ON " + airQualitySensorDtos.get(0).getDate());
        gridAirQualitySensor.setItems(airQualitySensorDtos);
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

//    private void upadte() {
//        gridStation.setItems(alertService.findByCity(textField.getValue()));
//    }

    private void styleComponents() {
        header1.getStyle()
                .set("border", "3px solid black")
                .set("color", "black")
                .set("margin", "-35px 0px 0px 480px");

        header2.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black")
                .set("margin", "20px 0px 0px 0px");
        header2.setWidth("543px");

        header3.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black")
                .set("margin", "5px 0px 0px 0px");
        header3.setWidth("543px");

        buttonSignUp.getStyle()
                .set("color", "black");

        buttonLogIn.getStyle()
                .set("color", "black");

        buttonLogOut.getStyle()
                .set("color", "black");

        buttonUpdateAccount.getStyle()
                .set("color", "black");

        buttonInfo.getStyle()
                .set("color", "black");

        selectCity.getStyle()
                .set("border", "1px solid black");
        selectCity.setHelperText("SELECT CITY");

        buttonSubscribe.getStyle()
                .set("border", "1px solid black")
                .set("color", "black")
                .set("background", "darkgrey");
        buttonSubscribe.setWidth("160px");
        buttonSubscribe.setHeight("45px");
        buttonSubscribe.setIcon(new Icon(VaadinIcon.ALARM));

        buttonDetails.getStyle()
                .set("border", "1px solid black")
                .set("color", "black")
                .set("background", "darkgrey");
        buttonDetails.setWidth("160px");
        buttonDetails.setHeight("45px");
        buttonDetails.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_RIGHT_O));

        labelGridTitle.getStyle()
                .set("border", "3px solid black")
                .set("background", "white")
                .set("color", "black");

        gridAirQualitySensor.getStyle()
                .set("border", "1px solid black")
                .set("color", "black");
        gridAirQualitySensor.setWidth("450px");
        gridAirQualitySensor.setHeight("200px");

        labelWeatherTitle.getStyle()
                .set("border", "3px solid black")
                .set("background", "white")
                .set("color", "black");

        labelTemperature.getStyle()
                .set("border", "1px solid black")
                .set("background", "white")
                .set("margin", "5px 0px 0px 0px")
                .set("color", "black");

        labelWindSpeed.getStyle()
                .set("border", "1px solid black")
                .set("background", "white")
                .set("margin", "5px 0px 0px 0px")
                .set("color", "black");

        labelHumidity.getStyle()
                .set("border", "1px solid black")
                .set("background", "white")
                .set("margin", "5px 0px 0px 0px")
                .set("color", "black");

        labelPressure.getStyle()
                .set("border", "1px solid black")
                .set("background", "white")
                .set("margin", "5px 0px 0px 0px")
                .set("color", "black");

        textFieldUsernameSignUp.getStyle()
                .set("border", "1px solid black");
        textFieldUsernameSignUp.setHelperText("ENTER USERNAME");
        textFieldUsernameSignUp.setWidth("300px");

        textFieldEmailSignUp.getStyle()
                .set("border", "1px solid black");
        textFieldEmailSignUp.setHelperText("ENTER E-MAIL");
        textFieldEmailSignUp.setWidth("300px");

        passwordFieldSignUp.getStyle()
                .set("border", "1px solid black");
        passwordFieldSignUp.setPlaceholder("ENTER PASSWORD");
        passwordFieldSignUp.setWidth("300px");

        textFieldEmailLogIn.getStyle()
                .set("border", "1px solid black");
        textFieldEmailLogIn.setHelperText("ENTER E-MAIL");
        textFieldEmailLogIn.setWidth("300px");

        passwordFieldLogIn.getStyle()
                .set("border", "1px solid black");
        passwordFieldLogIn.setPlaceholder("ENTER PASSWORD");
        passwordFieldSignUp.setWidth("300px");

        buttonCommitSignUp.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");

        buttonCommitLogIn.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");

        textFieldGiveEmailForSubscribe.getStyle()
//                .set("border", "1px solid black")
                .set("margin", "0px 0px 0px 20px")
                .set("color", "black");
        textFieldGiveEmailForSubscribe.setWidth("300px");
        textFieldGiveEmailForSubscribe.setHelperText("GIVE YOUR E-MAIL");

        buttonSubscribeCommit.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");

        buttonDeleteSubscribe.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");

        dialogInfo.setWidth("650px");
    }

    private void loadSelectData() {
        List<AirQualityStationDto> stationDtoList = alertService.getAllAirQualityStations();
        selectCity.setItemLabelGenerator(AirQualityStationDto::getCity);
        selectCity.setItems(stationDtoList);
    }

    private void setGridColumns() {
        gridAirQualitySensor.setColumns("name", "code", "value");
        gridAirQualitySensor.getColumnByKey("name").setHeader("POLLUTION NAME");
        gridAirQualitySensor.getColumnByKey("code").setHeader("PATTERN");
        gridAirQualitySensor.getColumnByKey("value").setHeader("VALUE [µg/m³]");
    }

//    private boolean signUp() {
//        String email = emailSignUpTextField.getValue();
//        if (alertService.getUserByEmail(email) == null) return false;
//        UserDto userDto = UserDto.builder()
//                .username(usernameSignUpTextField.getValue())
//                .email(emailSignUpTextField.getValue())
//                .password(passwordSignUpTextField.getValue())
//                .build();
//        alertService.createUser(userDto);
//        return true;
//    }

//    private boolean logIn() {
//        String username = usernameLogInTextField.getValue();
//        String password = passwordLogInTextField.getValue();
//        List<UserDto> userDtoList = alertService.getAllUsers();
//        if (userDtoList.isEmpty()) {
//            System.out.println("LIST IS EMPTY :(");
//            responseStatus.setText("THERE ARE NO USRES");
//            return false;
//        }
//        List<String> usernameList = new ArrayList<>();
//        for (UserDto userDto : userDtoList) {
//            usernameList.add(userDto.getUsername());
//        }
//        if (!usernameList.contains(username)) {
//            System.out.println("WRONG USER");
//            responseStatus.setText("WRONG USER");
//            return false;
//        }
//        alertService.logInUser(username, password);
//        if (!alertService.getUserByUsername(username).getPassword().equals(password)) {
//            System.out.println("WRONG PASSWORD");
//            responseStatus.setText("WRONG PASSWORD");
//            return false;
//        }
//        return true;
//    }

    private void showDialog(String text) {
        dialog.removeAll();
        dialog.add(new Text(text));
        dialog.open();
    }

    private void refresh() {
        alertService.setAllWeatherStations();
        alertService.setAllAirQualityStations();
    }
}
