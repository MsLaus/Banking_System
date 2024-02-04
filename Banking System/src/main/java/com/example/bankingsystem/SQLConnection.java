package com.example.bankingsystem;

import java.sql.*;

public class SQLConnection {

    protected Connection connection(String dbname, String user, String pass){
        Connection conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname,user,pass);
            if(conn!=null){
                System.out.println("Connection Established");
            }else{
                System.out.println("Connection failed.");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }

    protected void createTransactionTable(Connection conn, String table_name){
        Statement statement;
        try{
            String query ="CREATE TABLE "+ table_name+" (account_number VARCHAR(50), transaction_type VARCHAR(50), amount INT, date VARCHAR(50) )";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table created");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    protected void insertTransaction(Connection conn, String account_number, String transaction, int amount, String date){
        Statement statement;
        try{
            String query = String.format("insert into transactions (account_number, transaction_type, amount, date) values ('%s', '%s', %d, '%s');", account_number, transaction, amount, date);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row inserted");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    protected void createAccountsTable(Connection conn, String table_name){
        Statement statement;
        try{
            String query ="CREATE TABLE "+ table_name+" (first_name VARCHAR(50), last_name VARCHAR(50), phone_number VARCHAR(50), " +
                    "account_number VARCHAR(50), balance INT, YOB INT, email VARCHAR(50), pin INT)";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table created");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    protected void insert_row(Connection conn, String first_name, String last_name, String phone_number, String account_number, int balance, int YOB, String email, int pin){
        Statement statement;
        try{
            String query = String.format("insert into accounts " +
                            "(first_name, last_name, phone_number, account_number, balance, YOB, email, pin) values " +
                            "('%s', '%s', '%s', '%s', %d, %d, '%s', %d);",
                    first_name, last_name, phone_number, account_number, balance, YOB, email, pin);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row inserted");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**checks if the number account is in the database*/
    protected boolean search_account_number(Connection conn, String account_number){
        Statement statement;
        ResultSet rs;
        try {
            String query = String.format("select * from accounts where account_number = '%s'", account_number);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(rs.next()){
                return true;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return false;

    }

    /**checks if the number account or pin is correct*/
    protected boolean validate(Connection conn, String account_number, int pin){
        Statement statement;
        ResultSet rs ;
        try {
            String query = String.format(" SELECT * FROM accounts WHERE account_number = '%s' AND pin = %d ", account_number, pin);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(!rs.next()){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    protected void updateBalance(Connection conn, int newBalance, String account_number){
        Statement statement;
        try{
            String query = String.format("update accounts set balance = %d where account_number = '%s'", newBalance, account_number);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data updated");
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    protected String getBalance(Connection conn, String account_number){
        Statement statement;
        ResultSet rs;
        String result = "";
        try{
            String query = String.format("select balance from accounts where account_number = '%s'", account_number);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()){
                result = rs.getString("balance");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return result;

    }

    protected void deleteAccount(Connection conn, String account_number){
        Statement statement;
        ResultSet rs;
        try{
            String query = String.format("delete from accounts where account_number = '%s'", account_number);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            System.out.println("Row deleted.");

        }catch (Exception e){
            System.out.println(e);
        }
    }

    protected String getName (Connection conn, String account_number) {

        Statement statement;
        ResultSet rs = null;
        String result1 = "";
        String result2 = "";
        try{
            String query = String.format("select first_name from accounts where account_number = '%s'", account_number);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()){
                result1 = rs.getString("first_name");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            String query = String.format("select last_name from accounts where account_number = '%s'", account_number);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()){
                result2 = rs.getString("last_name");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        String result = result1 +" " + result2;
        return result;

    }

    protected String getNumberAccount(Connection conn, String name){
        int index = name.indexOf(" ");

        String firstName = name.substring(0, index);
        String lastName = name.substring(index, name.length());

        Statement statement;
        ResultSet rs = null;
        String result = "";
        try{
            String query = String.format("select account_number from accounts where first_name = '%s' and last_name = '%s'", firstName, lastName);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()){
                result = rs.getString("account_number");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return result;

    }

    protected void changeEmail(Connection conn, String email, String number_account){

        Statement statement;
        try{
            String query = String.format("update accounts set email = '%s' where account_number = '%s'", email, number_account);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data updated");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    protected void changePhoneNumber(Connection conn, String phone_number, String number_account){

        Statement statement;
        try{
            String query = String.format("update accounts set phone_number = '%s' where account_number = '%s'", phone_number, number_account);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data updated");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    protected void changePIN(Connection conn, int pin, String number_account){

        Statement statement;
        try{
            String query = String.format("update accounts set pin = %d where account_number = '%s'", pin, number_account);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data updated");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
   /**Checks if there are more than 10 transactions made in the current date, returns true if there are less than 10 */
    protected boolean underLimit(Connection conn, String account_number, String date) {

        Statement statement;
        ResultSet resultSet;
        int result;
        try {
            String query = String.format(" SELECT COUNT(*) FROM transactions WHERE account_number = '%s' and date ='%s'", account_number, date);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                result = resultSet.getInt(1);
                if (result < 10) return true;
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return false;

    }


}

