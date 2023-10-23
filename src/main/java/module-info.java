module com.example.btl1_dictionary {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.btl1_dictionary to javafx.fxml;
    exports com.example.btl1_dictionary;
}