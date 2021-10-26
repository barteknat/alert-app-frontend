package com.alert.app.frontend.view;

import com.alert.app.frontend.dto.UserDto;
import com.alert.app.frontend.service.AlertService;
import com.alert.app.frontend.status.UserStatus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Route(value = "login")
@StyleSheet("../frontend/styles/style.css")
public class LogInView extends VerticalLayout {

    private final MainView mainView;
    private final AlertService alertService;
    private final H1 header = new H1("AIR QUALITY ALERT APP");
    private final TextField email = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button commitLogIn = new Button("LOG IN");
    private final Button goToMainView = new Button("MAIN VIEW");

    public LogInView(MainView mainView, AlertService alertService) {
        this.mainView = mainView;
        this.alertService = alertService;
        this.addClassName("body");
        styleComponents();

        commitLogIn.addClickListener(event -> {
            if (logIn()) {
                commitLogIn.getUI().ifPresent(ui -> ui.navigate(""));
            }
        });

        goToMainView.addClickListener(event -> {
            goToMainView.getUI().ifPresent(ui -> ui.navigate(""));
        });

        VerticalLayout verticalLayout = new VerticalLayout(email, password, commitLogIn, goToMainView);
        add(header, verticalLayout);
    }

    private boolean logIn() {
        if (email.getValue().isEmpty() || password.getValue().isEmpty()) {
            mainView.showDialog("FIELD CANNOT BE EMPTY");
            return false;
        }
        if (email.getValue().equals("ADMIN") && password.getValue().equals("ADMINPASSWORD")) {
            commitLogIn.getUI().ifPresent(ui -> ui.navigate("admin"));
            mainView.showDialog("YOU ARE AT STATISTICS PANEL");
            return false;
        }
        UserDto userDto = alertService.getUserByEmail(email.getValue());
        if (userDto == null) {
            mainView.showDialog("INCORRECT USER");
            return false;
        }
        if (userDto.getLogStatus().equals(UserStatus.LOGGED_IN)) {
            mainView.showDialog("USER IS ALREADY LOGGED IN");
            return false;
        }
        if (!userDto.getPassword().equals(password.getValue())) {
            mainView.showDialog("INCORRECT PASSWORD");
            return false;
        }
        alertService.logInUser(email.getValue(), password.getValue());
        mainView.setLoggedInLayer();
        mainView.setUserDto(alertService.getUserByEmail(email.getValue()));
        mainView.showDialog("YOU ARE LOGGED IN");
        return true;
    }

    private void styleComponents() {
        header.getStyle()
                .set("border", "2px solid black")
                .set("color", "black");
        email.getStyle()
                .set("border", "1px solid black");
        email.setHelperText("ENTER E-MAIL");
        email.setWidth("300px");
        password.getStyle()
                .set("border", "1px solid black");
        password.setPlaceholder("ENTER PASSWORD");
        password.setWidth("300px");
        commitLogIn.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");
        commitLogIn.setWidth("150px");
        commitLogIn.setHeight("45px");
        goToMainView.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");
        goToMainView.setWidth("150px");
        goToMainView.setHeight("45px");
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, header);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, email);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, password);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, commitLogIn);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, goToMainView);
    }
}
