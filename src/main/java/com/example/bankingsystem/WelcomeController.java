package com.example.bankingsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController extends SQLConnection {


    private Stage stage;
    private Scene scene;

    public void signIn(ActionEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sign-in-view.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logIn(ActionEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("log-in-view.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
}