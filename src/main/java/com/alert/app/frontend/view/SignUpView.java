package com.alert.app.frontend.view;

import com.alert.app.frontend.dto.UserDto;
import com.alert.app.frontend.service.AlertService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Route(value = "signup")
@StyleSheet("../frontend/styles/style.css")
public class SignUpView extends VerticalLayout {

    private final MainView mainView;
    private final AlertService alertService;
    private final H1 header = new H1("AIR QUALITY ALERT APP");
    private final TextField username = new TextField();
    private final TextField email = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button commitSignUp = new Button("SIGN UP");
    private final Button goToMainView = new Button("MAIN VIEW");

    public SignUpView(MainView mainView, AlertService alertService) {
        this.mainView = mainView;
        this.alertService = alertService;
        this.addClassNames("body");
        styleComponents();
        commitSignUp.addClickListener(event -> {
            if (signUp()) {
                commitSignUp.getUI().ifPresent(ui -> ui.navigate(""));
            }
        });
        goToMainView.addClickListener(event -> {
            goToMainView.getUI().ifPresent(ui -> ui.navigate(""));
        });
        VerticalLayout verticalLayout = new VerticalLayout(username, email, password, commitSignUp, goToMainView);
        add(header, verticalLayout);
    }

    private boolean signUp() {
        if (username.getValue().isEmpty() || email.getValue().isEmpty() || password.getValue().isEmpty()) {
            mainView.showDialog("FIELD CANNOT BE EMPTY");
            return false;
        }
        if (password.getValue().length() < 3 || password.getValue().length() > 15) {
            mainView.showDialog("PASSWORD IS MIN 3 AND MAX 15 CHARACTERS");
            return false;
        }
        UserDto userDto = alertService.getUserByEmail(email.getValue());
        if (userDto != null) {
            mainView.showDialog("USER WITH THIS E-MAIL ALREADY EXISTS IN DATABASE");
            return false;
        }
        alertService.createUser(UserDto.builder()
                .username(username.getValue())
                .email(email.getValue())
                .password(password.getValue())
                .build());
        mainView.showDialog("ACCOUNT HAS BEEN CREATED");
        return true;
    }

    private void styleComponents() {
        header.getStyle()
                .set("border", "3px solid black")
                .set("color", "black");
        username.getStyle()
                .set("border", "1px solid black");
        username.setHelperText("ENTER USERNAME");
        username.setWidth("300px");
        email.getStyle()
                .set("border", "1px solid black");
        email.setHelperText("ENTER E-MAIL");
        email.setWidth("300px");
        password.getStyle()
                .set("border", "1px solid black");
        password.setPlaceholder("ENTER PASSWORD");
        password.setWidth("300px");
        commitSignUp.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");
        goToMainView.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");
        setHorizontalComponentAlignment(Alignment.CENTER, header);
        setHorizontalComponentAlignment(Alignment.CENTER, username);
        setHorizontalComponentAlignment(Alignment.CENTER, email);
        setHorizontalComponentAlignment(Alignment.CENTER, password);
        setHorizontalComponentAlignment(Alignment.CENTER, commitSignUp);
        setHorizontalComponentAlignment(Alignment.CENTER, goToMainView);
    }
}
