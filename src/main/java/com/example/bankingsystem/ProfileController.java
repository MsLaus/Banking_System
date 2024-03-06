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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    SQLConnection db = new SQLConnection();
    Connection conn = db.connection("Bank System", "postgres", "projectspassword");

    @FXML
    private Label balance;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label nrAcc;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField pinTextField;
    User user = new User();
    private String ACCOUNT_NUMBER = user.getAccount_number();

    private Stage stage;
    private Scene scene;


    public void logOut(ActionEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome-view.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void profile(ActionEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void dashboard(ActionEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    
    public void report(ActionEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("report-view.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } 

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nrAcc.setText(ACCOUNT_NUMBER);
        nameLabel.setText(db.getName(conn, ACCOUNT_NUMBER));
        balance.setText(db.getBalance(conn, ACCOUNT_NUMBER));

    }

    public void deleteAccount(ActionEvent e) throws IOException{

        db.deleteAccount(conn, ACCOUNT_NUMBER);

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("You deleted your account.");
        dialog.setContentText("The account is deleted.");
        ButtonType type = new ButtonType("Ok");
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.show();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome-view.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void changeAction(){

        if(!emailTextField.getText().isEmpty()){
            db.changeEmail(conn, emailTextField.getText(), ACCOUNT_NUMBER);

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You changed your email.");
            dialog.setContentText("Your email is changed.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();
        }

        if(!phoneTextField.getText().isEmpty()){
            db.changePhoneNumber(conn, phoneTextField.getText(), ACCOUNT_NUMBER);

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You changed your phone number.");
            dialog.setContentText("Your phone number is changed.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();
        }

        if(!pinTextField.getText().isEmpty()){
            db.changePIN(conn, pinTextField.getText(), ACCOUNT_NUMBER);

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You changed your pin.");
            dialog.setContentText("Your pin is changed.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();
        }
    }
}
