module com.example.bankingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires jbcrypt;


    opens com.example.bankingsystem to javafx.fxml;
    exports com.example.bankingsystem;
}