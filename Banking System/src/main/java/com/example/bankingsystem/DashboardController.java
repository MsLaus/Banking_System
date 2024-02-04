package com.example.bankingsystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    SQLConnection db = new SQLConnection();
    Connection conn = db.connection("Bank System", "postgres", "lenovo24");

    private Stage stage;
    private Scene scene;

    @FXML
    private TableView<Transaction> tableView;

    @FXML
    private TableColumn<Transaction, String> transaction;

    @FXML
    private TableColumn<Transaction, String> amount;

    @FXML
    private TableColumn<Transaction, String> date;


    @FXML
    private Label nameLabel, balanceLabel, numberAccountLabel, dateLabel;

    @FXML
    ChoiceBox<String> depositChooser;

    @FXML
    ChoiceBox<String> withdrawalChooser;

    private final String[] BALANCE = {"10", "100", "200", "500", "1000"};

    final String NUMBER_ACCOUNT = AccountNumberHelper.accountHelper;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    LocalDateTime now = LocalDateTime.now();

    String query = null;
    PreparedStatement preparedStatement =null;
    ResultSet resultSet = null;

    ObservableList<Transaction> list = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //sets the values of the labels
        dateLabel.setText(dtf.format(now));
        numberAccountLabel.setText(NUMBER_ACCOUNT);
        nameLabel.setText(db.getName(conn, NUMBER_ACCOUNT));
        balanceLabel.setText(db.getBalance(conn, NUMBER_ACCOUNT));

        // sets the value for depositChooser and withdrawalChooser
        depositChooser.getItems().addAll(BALANCE);
        withdrawalChooser.getItems().addAll(BALANCE);

        //add transactions to the tableview
        try {
            refreshTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        transaction.setCellValueFactory(transaction_type -> new SimpleStringProperty(transaction_type.getValue().getTypeTransaction()));
        amount.setCellValueFactory(amount -> new SimpleStringProperty(amount.getValue().getAmount()));
        date.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));

    }

    @FXML
    private void refreshTable() throws SQLException {
        list.clear();
        query = "SELECT * FROM transactions";
        preparedStatement = conn.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            list.add(new Transaction
                    (resultSet.getString("transaction_type"),
                    resultSet.getString("amount"),
                    resultSet.getString("date")));

            tableView.setItems(list);
        }
    }

    public void deposit() throws SQLException {

        if(depositChooser.getValue() == null){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.setContentText("Please choose the amount of money you want to deposit.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();
        }else {

            int current_balance = Integer.parseInt(db.getBalance(conn, String.valueOf(NUMBER_ACCOUNT)));
            int deposit = Integer.parseInt(depositChooser.getValue());
            int newBalance = current_balance+deposit;

            if(!db.underLimit(conn, NUMBER_ACCOUNT, dtf.format(now))){
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("You can not do this transaction.");
                dialog.setContentText("The limit of transaction is 10. You can do another transaction tomorrow.");
                ButtonType type = new ButtonType("Ok");
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.show();
            }else {
                db.updateBalance(conn, newBalance, String.valueOf(NUMBER_ACCOUNT));
                db.insertTransaction(conn, NUMBER_ACCOUNT, "deposit", Integer.parseInt(depositChooser.getValue()), dtf.format(now));
                balanceLabel.setText(db.getBalance(conn, NUMBER_ACCOUNT));
                refreshTable();
            }
        }


    }

    public void withdrawal() throws SQLException{

        if(withdrawalChooser.getValue() == null){

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.setContentText("Please choose the amount of money you want to withdrawal.");
            ButtonType type = new ButtonType("Ok");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();
        }else{

            int currentBalance = Integer.parseInt(db.getBalance(conn, NUMBER_ACCOUNT));
            int newBalance = currentBalance - Integer.parseInt(withdrawalChooser.getValue());

            if (newBalance <0) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("You can not do this transaction.");
                dialog.setContentText("You do not have enough money.");
                ButtonType type = new ButtonType("Ok");
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.show();
            }else{
                db.updateBalance(conn, newBalance, NUMBER_ACCOUNT);

                db.insertTransaction(conn, NUMBER_ACCOUNT, "withdrawal", Integer.parseInt(withdrawalChooser.getValue()), dtf.format(now));

            }

            balanceLabel.setText(db.getBalance(conn, NUMBER_ACCOUNT));
            refreshTable();
        }


    }

    public void report(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("report-view.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
}
