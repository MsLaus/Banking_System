package com.example.bankingsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.Year;
import java.util.Random;
import java.util.ResourceBundle;

public class SignInController extends  SQLConnection implements Initializable {

    SQLConnection db = new SQLConnection();
    Connection conn = db.connection("Bank System", "postgres", "lenovo24");

    @FXML
    private TextField accountNumberTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private ChoiceBox<String> initialBalanceChooser;

    private final String[] BALANCE = { "10", "50", "100", "250", "500"};

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField pinTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button generateButton;

    Stage stage;
    Scene scene;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pinTextField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {

            if (!isNumeric(keyEvent.getCharacter())){
                keyEvent.consume();
            }
        });

        initialBalanceChooser.getItems().addAll(BALANCE);

    }

    private boolean isNumeric(String s){

        return s.matches("\\d*");
    }

    public void generateNumberAccount(){

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // first not 0 digit
        sb.append(random.nextInt(9) + 1);

        // rest of 11 digits
        for (int i = 0; i < 11; i++) {
            sb.append(random.nextInt(10));
        }

        if(!search_account_number(conn, String.valueOf(Long.valueOf(sb.toString()).longValue()))){
            accountNumberTextField.setText(String.valueOf(Long.valueOf(sb.toString()).longValue()));
            generateButton.setDisable(true);
        }else {
            generateNumberAccount();
            System.out.println("error");}
    }

    public void createAccount(ActionEvent e) throws IOException {

        if( firstNameTextField.getText().isEmpty()){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You need to complete all the fields");
            dialog.setContentText("You need to write your first name.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();

        }

        if( lastNameTextField.getText().isEmpty()){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You need to complete all the fields");
            dialog.setContentText("You need to write your last name.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();

        }

        if(phoneNumberTextField.getText().isEmpty()){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You need to complete all the fields");
            dialog.setContentText("You need to write your phone number.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();

        }

        if( accountNumberTextField.getText().isEmpty()){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You need to complete all the fields");
            dialog.setContentText("You need to generate the account number.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();

        }

        if( initialBalanceChooser.getValue().isEmpty()){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You need to complete all the fields");
            dialog.setContentText("You need to choose your initial balance.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();

        }

        if( datePicker.getValue().toString().isEmpty()){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You need to complete all the fields");
            dialog.setContentText("You need to enter your date of birth.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();

        }

        if( emailTextField.getText().isEmpty()){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You need to complete all the fields");
            dialog.setContentText("You need to write your email.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();

        }

        if( pinTextField.getText().isEmpty() || pinTextField.getText().length() < 4){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You need to complete all the fields");
            dialog.setContentText("You need to write a 4 digit pin.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();

        }

        final String FIRSTNAME = firstNameTextField.getText();
        final String LASTNAME = lastNameTextField.getText();
        final String PHONE_NUMBER = phoneNumberTextField.getText();
        final String ACCOUNT_NUMBER = accountNumberTextField.getText();
        final int BALANCE = Integer.parseInt(initialBalanceChooser.getValue());
        final int YOB = datePicker.getValue().getYear();
        final String EMAIL = emailTextField.getText();
        final String PIN = pinTextField.getText();


        if(isAdult() && !FIRSTNAME.isEmpty() && !LASTNAME.isEmpty() && !PHONE_NUMBER.isEmpty() && !ACCOUNT_NUMBER.isEmpty()
           && !String.valueOf(BALANCE).isEmpty() && !String.valueOf(YOB).isEmpty() && !EMAIL.isEmpty() && !PIN.isEmpty()){

            insert_row(conn, FIRSTNAME, LASTNAME, PHONE_NUMBER, ACCOUNT_NUMBER, BALANCE, YOB, EMAIL, PIN);

            User user = new User(ACCOUNT_NUMBER, FIRSTNAME, LASTNAME, BALANCE);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    public boolean isAdult(){

        int YOB = datePicker.getValue().getYear();
        int currentYear = Year.now().getValue();

        int age = currentYear - YOB;

        if(age < 18){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("You need to be over 18!");
            dialog.setContentText("You need to be an adult to open a bank account.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();
            return false;
        }else return  true;
    }
}
