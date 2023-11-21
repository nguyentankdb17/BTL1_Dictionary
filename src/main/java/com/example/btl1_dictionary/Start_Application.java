package com.example.btl1_dictionary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.gimpy.FishEyeGimpyRenderer;

import java.awt.image.BufferedImage;
import java.io.*;

public class Start_Application extends Application {

    private Captcha captcha;
    private TextField captchaInput;
    private ImageView captchaImageView;

    private final Image Background_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Background.png").toExternalForm());
    BackgroundImage backgroundImage = new BackgroundImage(Background_Image,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);

    Background background = new Background(backgroundImage);

    @Override
    public void start(Stage stage) throws IOException {
        try {
            captcha = new Captcha.Builder(200, 50)
                    .addText()
                    .addBackground(new GradiatedBackgroundProducer())
                    .gimp(new FishEyeGimpyRenderer())
                    .addBorder()
                    .build();

            Label captchaLabel = new Label("COMPLETE THIS CAPTCHA:");
            Font font = new Font("Segoe UI Black",20);
            captchaLabel.setFont(font);
            captchaImageView = new ImageView(convertToJavaFXImage(captcha.getImage()));
            Button resetButton = new Button("Reset");
            resetButton.setOnAction(e -> resetCaptcha());
            captchaInput = new TextField();
            captchaInput.setMaxWidth(300);

            Button submitButton = new Button("SUBMIT");
            submitButton.setOnAction(e -> {
                try {
                    verifyCaptcha(stage);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });


            VBox captchaControls = new VBox(10);
            captchaControls.getChildren().addAll(captchaImageView, resetButton);
            captchaControls.setAlignment(Pos.CENTER);

            VBox captchaLayout = new VBox(10);
            captchaLayout.getChildren().addAll(captchaLabel, captchaControls, captchaInput, submitButton);
            captchaLayout.setAlignment(Pos.CENTER);

            BorderPane root = new BorderPane();
            root.setBackground(background);
            root.setRight(captchaLayout);


            Scene scene = new Scene(root, 875, 650);


            stage.setTitle("DICTIONARY");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }


    private void verifyCaptcha(Stage stage) throws IOException {
        String userInput = captchaInput.getText().trim();

        if (captcha.isCorrect(userInput)) {
            System.out.println("CAPTCHA is correct!");
            switchToMain(stage);
        } else {
            System.out.println("CAPTCHA is incorrect. Please try again.");
            resetCaptcha();
        }
    }

    private void resetCaptcha() {
        captcha = new Captcha.Builder(200, 50)
                .addText()
                .addBackground(new GradiatedBackgroundProducer())
                .gimp(new FishEyeGimpyRenderer())
                .addBorder()
                .build();
        captchaInput.setText("");
        captchaImageView.setImage(convertToJavaFXImage(captcha.getImage()));
    }

    private Image convertToJavaFXImage(BufferedImage bufferedImage) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            javax.imageio.ImageIO.write(bufferedImage, "png", out);
            try (InputStream in = new ByteArrayInputStream(out.toByteArray())) {
                return new Image(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void switchToMain(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML File/search.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 875, 650);
            stage.setTitle("DICTIONARY");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}