package com.example.bankingsystem;

public class User {

    private static  String account_number;
    private  static String first_name;
    private  static String last_name;
    private int balance;

    public User(String account_number, String first_name, String last_name, int balance) {
        User.account_number = account_number;
        User.first_name = first_name;
        User.last_name = last_name;
        this.balance = balance;
    }

    public User(){}

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        User.account_number = account_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        User.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        User.last_name = last_name;
    }

}
