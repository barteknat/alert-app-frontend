package com.alert.app.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SpringBootApplication
public class AlertFrontApplication implements ActionListener {

    public static void main(String[] args) {
        SpringApplication.run(AlertFrontApplication.class, args);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
