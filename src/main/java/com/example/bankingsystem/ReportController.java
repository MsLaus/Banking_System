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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    SQLConnection db = new SQLConnection();
    Connection conn = db.connection("Bank System", "postgres", "lenovo24");
    User user = new User();

    @FXML
    private Stage stage;

    @FXML
    private Scene scene;

    @FXML
    private Label nameLabel;
    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.setText(db.getName(conn, user.getAccount_number()));
    }
    public void reportAction(){
        try {
            //Create a file named after the user's name + report
            FileWriter myWriter = new FileWriter(nameLabel.getText()+"Report");
            myWriter.write(textArea.getText());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Thank you for reporting an issue!");
            dialog.setContentText("The issue you reported was send to the developer.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

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

}
