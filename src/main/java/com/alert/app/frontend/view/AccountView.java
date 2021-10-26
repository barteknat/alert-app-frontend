package com.alert.app.frontend.view;

import com.alert.app.frontend.dto.SubscribeDto;
import com.alert.app.frontend.dto.UserDto;
import com.alert.app.frontend.service.AlertService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.List;

@PreserveOnRefresh
@UIScope
@Route(value = "account")
@StyleSheet("../frontend/styles/style.css")
public class AccountView extends HorizontalLayout {

    private final MainView mainView;
    private final AlertService alertService;
    private final H1 header = new H1("AIR QUALITY ALERT APP");
    private final Label presentUsername = new Label();
    private final Label presentEmail = new Label();
    private final Label presentPassword = new Label();
    private final TextField newUsername = new TextField();
    private final TextField newEmail = new TextField();
    private final PasswordField newPassword = new PasswordField();
    private final Button updateAccount = new Button("UPDATE ACCOUNT");
    private final Button deleteAccount = new Button("DELETE ACCOUNT");
    private final Button showSubscribes = new Button("SHOW SUBSCRIBES");
    private final Button hideSubscribes = new Button("HIDE SUBSCRIBES");
    private final Button deleteSubscribe = new Button("DELETE SUBSCRIBE");
    private final Button goToMainView = new Button("MAIN VIEW");
    private final Button deleteAccountCommit = new Button("COMMIT DELETE");
    private final Grid<SubscribeDto> subscribesGrid = new Grid<>(SubscribeDto.class);
    private final VerticalLayout verticalLayout = new VerticalLayout(presentUsername, presentEmail, presentPassword);
    private final VerticalLayout verticalLayout2 = new VerticalLayout(newUsername, newEmail, newPassword);
    private final VerticalLayout verticalLayout3 = new VerticalLayout(updateAccount, deleteAccount, deleteAccountCommit);
    private final HorizontalLayout horizontalLayout = new HorizontalLayout(verticalLayout, verticalLayout2, verticalLayout3);
    private final HorizontalLayout horizontalLayout2 = new HorizontalLayout(showSubscribes, subscribesGrid, deleteSubscribe, hideSubscribes);

    public AccountView(MainView mainView, AlertService alertService) {
        this.mainView = mainView;
        this.alertService = alertService;
        this.addClassNames("body");
        styleComponents();
        loadLoggedUser();
        setSubscribesGrid();
        subscribesGrid.setVisible(false);
        deleteSubscribe.setVisible(false);
        hideSubscribes.setVisible(false);
        deleteAccountCommit.setVisible(false);

        showSubscribes.addClickListener(event -> {
            if (setSubscribes()) {
                subscribesGrid.setVisible(true);
                deleteSubscribe.setVisible(true);
                hideSubscribes.setVisible(true);
            }
        });

        deleteSubscribe.addClickListener(event -> {
            deleteSubscribe();
        });

        updateAccount.addClickListener(event -> {
            if (update()) {
                updateAccount.getUI().ifPresent(ui -> ui.navigate("login"));
            }
        });

        deleteAccount.addClickListener(event -> {
            deleteAccountCommit.setVisible(true);
        });

        deleteAccountCommit.addClickListener(event -> {
            if (deleteAccount()) {
                deleteAccountCommit.getUI().ifPresent(ui -> ui.navigate(""));
            }
        });

        hideSubscribes.addClickListener(event -> {
            subscribesGrid.setVisible(false);
            deleteSubscribe.setVisible(false);
            hideSubscribes.setVisible(false);
        });

        goToMainView.addClickListener(event -> {
            goToMainView.getUI().ifPresent(ui -> ui.navigate(""));
        });

        VerticalLayout mainVerticalLayout = new VerticalLayout(header, horizontalLayout, horizontalLayout2, goToMainView);
        add(mainVerticalLayout);
    }

    private boolean update() {
        UserDto presentUserDto = mainView.getUserDto();
        if (presentUserDto == null) return false;
        alertService.logOutUser(presentUserDto.getEmail());
        if (newUsername.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
            mainView.showDialog("FIELD CANNOT BE EMPTY");
            return false;
        }
        UserDto newUserDto = UserDto.builder()
                .id(presentUserDto.getId())
                .username(newUsername.getValue())
                .email(newEmail.getValue())
                .password(newPassword.getValue())
                .build();
        alertService.updateUser(newUserDto);
        UserDto updatedUserDto = alertService.getUserByEmail(newUserDto.getEmail());
        if (updatedUserDto == null) return false;
        mainView.showDialog("YOUR PERSONAL DETAILS HAS BEEN UPDATED");
        return true;
    }

    private void deleteSubscribe() {
        SubscribeDto subscribeDto = subscribesGrid.getSelectionModel().getFirstSelectedItem().orElse(null);
        if (subscribeDto == null) {
            mainView.showDialog("YOU HAVE TO CHOOSE CITY FROM LIST FIRST");
            return;
        }
        alertService.deleteSubscribe(subscribeDto.getId());
        if (alertService.getSubscribeByUserIdAndCity(mainView.getUserDto().getId(), subscribeDto.getCity()) != null) return;
        mainView.showDialog("SUBSCRIBE IS REMOVED");
        List<SubscribeDto> subscribeDtoList = alertService.getAllSubscribesByUserId(mainView.getUserDto().getId());
        subscribesGrid.setItems(subscribeDtoList);
        if (subscribeDtoList.isEmpty()) {
            subscribesGrid.setVisible(false);
            deleteSubscribe.setVisible(false);
            hideSubscribes.setVisible(false);
        }
    }

    private boolean deleteAccount() {
        List<SubscribeDto> subscribeDtoList = alertService.getAllSubscribesByUserId(mainView.getUserDto().getId());
        if (!subscribeDtoList.isEmpty()) {
            for (SubscribeDto subscribeDto : subscribeDtoList) {
                alertService.deleteSubscribe(subscribeDto.getId());
            }
        }
        alertService.deleteUser(mainView.getUserDto().getId());
        mainView.setLoggedOutLayer();
        mainView.setUserDto(null);
        mainView.showDialog("ACCOUNT HAS BEEN REMOVED");
        return true;
    }

    private void setSubscribesGrid() {
        subscribesGrid.setColumns("city");
        subscribesGrid.getColumnByKey("city").setHeader("YOU ARE SUBSCRIBING");
    }

    private boolean setSubscribes() {
        UserDto userDto = mainView.getUserDto();
        if (userDto == null) return false;
        List<SubscribeDto> subscribeDtoList = alertService.getAllSubscribesByUserId(mainView.getUserDto().getId());
        if (subscribeDtoList == null) return false;
        if (subscribeDtoList.isEmpty()) {
            mainView.showDialog("YOU HAVE NO SUBSCRIBES");
            return false;
        }
        subscribesGrid.setItems(subscribeDtoList);
        return true;
    }

    private void loadLoggedUser() {
        UserDto userDto = mainView.getUserDto();
        if (userDto == null) return;
        presentUsername.setText(userDto.getUsername());
        presentEmail.setText(userDto.getEmail());
        presentPassword.setText(userDto.getPassword());
    }

    private void styleComponents() {
        header.getStyle()
                .set("border", "2px solid black")
                .set("color", "black");
        presentUsername.getStyle()
                .set("background", "grey")
                .set("font-size", "20px")
                .set("margin", "20px 0px 0px 0px")
                .set("border", "1px solid black");
        presentUsername.setWidth("300px");
        presentUsername.setHeight("40px");
        presentEmail.getStyle()
                .set("background", "grey")
                .set("font-size", "20px")
                .set("margin", "40px 0px 0px 0px")
                .set("border", "1px solid black");
        presentEmail.setWidth("300px");
        presentEmail.setHeight("40px");
        presentPassword.getStyle()
                .set("background", "grey")
                .set("font-size", "20px")
                .set("margin", "40px 0px 0px 0px")
                .set("border", "1px solid black");
        presentPassword.setWidth("300px");
        presentPassword.setHeight("40px");
        newUsername.getStyle()
                .set("border", "1px solid black");
        newUsername.setHelperText("ENTER NEW USERNAME");
        newUsername.setWidth("300px");
        newEmail.getStyle()
                .set("border", "1px solid black");
        newEmail.setHelperText("ENTER NEW E-MAIL");
        newEmail.setWidth("300px");
        newPassword.getStyle()
                .set("border", "1px solid black");
        newPassword.setPlaceholder("ENTER NEW PASSWORD");
        newPassword.setWidth("300px");
        subscribesGrid.getStyle()
                .set("border", "1px solid black")
                .set("color", "black");
        subscribesGrid.setWidth("220px");
        subscribesGrid.setHeight("200px");
        updateAccount.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("margin", "20px 0px 0px 0px")
                .set("color", "black");
        updateAccount.setWidth("200px");
        updateAccount.setHeight("45px");
        deleteAccount.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("margin", "40px 0px 0px 0px")
                .set("color", "black");
        deleteAccount.setWidth("200px");
        deleteAccount.setHeight("45px");
        deleteAccountCommit.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("margin", "20px 0px 0px 0px")
                .set("color", "black");
        deleteAccountCommit.setWidth("200px");
        deleteAccountCommit.setHeight("45px");
        showSubscribes.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");
        showSubscribes.setWidth("200px");
        showSubscribes.setHeight("45px");
        deleteSubscribe.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");
        deleteSubscribe.setWidth("200px");
        deleteSubscribe.setHeight("45px");
        hideSubscribes.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("color", "black");
        hideSubscribes.setWidth("200px");
        hideSubscribes.setHeight("45px");
        goToMainView.getStyle()
                .set("border", "1px solid black")
                .set("background", "grey")
                .set("margin", "20px 0px 0px 255px")
                .set("color", "black");
        goToMainView.setWidth("200px");
        goToMainView.setHeight("45px");
        horizontalLayout2.getStyle()
                .set("margin", "20px 0px 0px 255px");
        setVerticalComponentAlignment(Alignment.CENTER, header);
        setVerticalComponentAlignment(Alignment.CENTER, horizontalLayout);
    }
}
