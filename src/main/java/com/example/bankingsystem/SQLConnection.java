package com.example.bankingsystem;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class SQLConnection {

    Statement statement;
    ResultSet rs;
    String query;

    /*public static void main(String[] args) {
        Connection conn = connection("Bank System", "postgres", "lenovo24");
        createAccountsTable(conn, "accounts");
        createTransactionTable(conn, "transactions");
    }*/


    protected static Connection connection(String dbname, String user, String pass){
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
            e.printStackTrace();
        }
        return conn;
    }

    private static void createTransactionTable(Connection conn, String table_name){

        Statement statement;
        String query;
        try{
            query ="CREATE TABLE "+ table_name+" (account_number VARCHAR(50) NOT NULL, transaction_type VARCHAR(50) NOT NULL , amount INT NOT NULL, date VARCHAR(50) NOT NULL, FOREIGN KEY (account_number) REFERENCES accounts(account_number))";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table created");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void insertTransaction(Connection conn, String account_number, String transaction, int amount, String date){
        try{
            query = String.format("INSERT INTO transactions (account_number, transaction_type, amount, date) VALUES ('%s', '%s', %d, '%s');", account_number, transaction, amount, date);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row inserted");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createAccountsTable(Connection conn, String table_name){

        Statement statement;
        String query;
        try{
            query ="CREATE TABLE "+ table_name+" ( account_number VARCHAR(50) NOT NULL PRIMARY KEY, first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, phone_number VARCHAR(50), " +
                    " balance INT NOT NULL, YOB INT NOT NULL, email VARCHAR(50), pin VARCHAR(500) NOT NULL)";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table created");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void insert_row(Connection conn, String first_name, String last_name, String phone_number, String account_number, int balance, int YOB, String email, String pin){
        try{
            query = String.format("INSERT INTO accounts " +
                            "(first_name, last_name, phone_number, account_number, balance, YOB, email, pin) VALUES " +
                            "('%s', '%s', '%s', '%s', %d, %d, '%s', '%s');",
                    first_name, last_name, phone_number, account_number, balance, YOB, email, pin);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row inserted");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**checks if the number account is in the database*/
    protected boolean search_account_number(Connection conn, String account_number){
        try {
            query = String.format("SELECT * FROM accounts WHERE account_number = '%s'", account_number);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(rs.next()){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }

    protected User getUser(Connection conn, String account_number){

        User user = null;

        try{
            query = String.format("SELECT * FROM accounts WHERE account_number = '%s'", account_number);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()){
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                int balance = rs.getInt("balance");
                user = new User(account_number, first_name, last_name, balance);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return user;

    }

    /**checks if the number account or pin is correct*/
    protected boolean validate(Connection conn, String account_number, String pin){

        String result;
        try{
            query = String.format("SELECT pin FROM accounts WHERE account_number = '%s'", account_number);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()){
                result = rs.getString("pin");
                if(result == pin) return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
        return false;
        
    }

    protected void updateBalance(Connection conn, int newBalance, String account_number){
        try{
            query = String.format("UPDATE accounts SET balance = %d WHERE account_number = '%s'", newBalance, account_number);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data updated");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    protected String getBalance(Connection conn, String account_number){
        String result = "";
        try{
            query = String.format("SELECT balance FROM accounts WHERE account_number = '%s'", account_number);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()){
                result = rs.getString("balance");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    protected void deleteAccount(Connection conn, String account_number){
        try{
            query = String.format("DELETE FROM accounts WHERE account_number = '%s'", account_number);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            System.out.println("Row deleted.");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected String getName (Connection conn, String account_number) {
        String result1 = "";
        String result2 = "";
        try{
            query = String.format("SELECT first_name FROM accounts WHERE account_number = '%s'", account_number);
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
            query = String.format("SELECT last_name FROM accounts WHERE account_number = '%s'", account_number);
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

        String result = "";
        try{
            query = String.format("SELECT account_number FROM accounts WHERE first_name = '%s' AND last_name = '%s'", firstName, lastName);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()){
                result = rs.getString("account_number");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    protected void changeEmail(Connection conn, String email, String number_account){
        try{
            query = String.format("UPDATE accounts SET email = '%s' WHERE account_number = '%s'", email, number_account);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data updated");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void changePhoneNumber(Connection conn, String phone_number, String number_account){
        try{
            query = String.format("UPDATE accounts SET phone_number = '%s' WHERE account_number = '%s'", phone_number, number_account);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data updated");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void changePIN(Connection conn, String pin, String number_account){
        try{
            query = String.format("UPDATE accounts SET pin = '%s WHERE account_number = '%s'", pin, number_account);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data updated");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

   /**Checks if there are more than 10 transactions made in the current date, returns true if there are less than 10 */
    protected boolean underLimit(Connection conn, String account_number, String date) {
        int result;
        try {
            query = String.format(" SELECT COUNT(*) FROM transactions WHERE account_number = '%s' AND date ='%s'", account_number, date);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()) {
                result = rs.getInt(1);
                if (result < 10) return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}

