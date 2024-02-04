package com.example.bankingsystem;

public class Transaction{

    private String typeTransaction;
    private String amount;
    private String date;

    public Transaction(String typeTransaction, String amount, String date) {
        this.typeTransaction = typeTransaction;
        this.amount = amount;
        this.date = date;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public String getAmount() {
        return amount;
    }
}
