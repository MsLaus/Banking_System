package com.example.bankingsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class LogInController extends SQLConnection implements Initializable {

    SQLConnection db = new SQLConnection();
    Connection conn = db.connection("Bank System", "postgres", "lenovo24");

    @FXML
    private TextField accountNumberTextField;

    @FXML
    private TextField pinTextField;

    Scene scene;
    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pinTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("You need to enter only digits in the PIN field.");
                dialog.setContentText("Please enter digits in the PIN field.");
                ButtonType type = new ButtonType("Ok");
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.show();
            }
        });
    }

    public void logIn(ActionEvent e) throws IOException {

        final String ACCOUNT_NUMBER = accountNumberTextField.getText();
        final int PIN = Integer.parseInt(pinTextField.getText());

        if(accountNumberTextField.getText().isEmpty() || pinTextField.getText().isEmpty()){
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You need to complete all the fields");
            dialog.setContentText("You need to write your account number and pin.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();
        }

        if(!validate(conn, ACCOUNT_NUMBER, PIN)){

            AccountNumberHelper.setAccountHelper(ACCOUNT_NUMBER);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("The username or the password is wrong.");
            dialog.setContentText("Please check if the username or the password is correct.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();
        }
        
    }
}
