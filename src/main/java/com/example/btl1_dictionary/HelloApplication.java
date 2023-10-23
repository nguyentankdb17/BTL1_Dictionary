package com.example.btl1_dictionary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.*;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            Parent fxmlLoader = FXMLLoader.load(getClass().getResource("FXML File/sample.fxml"));
            Scene scene = new Scene(fxmlLoader, 900, 620);
            stage.setTitle("3L DICTIONARY");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    public static void main(String[] args) {
        Scanner scan  = new Scanner(System.in);
        int a = scan.nextInt();
        if(a==1){
            while(true) {
                int ac = scan.nextInt();
                TextToSpeech.TextTSP(ac);
            }
        }
        else {
            launch(args);
        }

    }
}