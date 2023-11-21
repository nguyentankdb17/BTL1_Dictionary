package com.example.btl1_dictionary;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import com.asprise.ocr.Ocr;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.net.*;

public class Google_Controller extends General_Controller {

    @FXML
    private ImageView langFrom;

    @FXML
    private final Image Vie = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Vietnam_Button.png").toExternalForm());

    @FXML
    private ImageView langTo;

    @FXML
    private final Image Eng = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/English_Button.png").toExternalForm());


    @FXML
    private ImageView voice_from;

    @FXML
    private ImageView voice_to;

    @FXML
    private final Image Voice_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Voice_Button.png").toExternalForm());

    @FXML
    private TextArea input;

    @FXML
    private TextArea output;

    @FXML
    private ImageView clear;

    @FXML
    private ImageView micro;

    @FXML
    private ImageView upload;

    private boolean isEnglish = true;

    private static String ConnectToGGAPI( String input, String languageFrom, String languageTo) throws IOException, IOException {
        String urlSource = "https://script.google.com/macros/s/AKfycby3AOWmhe32TgV9nW-Q0TyGOEyCHQeFiIn7hRgy5m8jHPaXDl2GdToyW_3Ys5MTbK6wjg/exec"
                + "?q=" + URLEncoder.encode(input, "UTF-8")
                + "&target=" + languageTo
                + "&source=" + languageFrom;
        URL url = new URL(urlSource);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder res = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            res.append(line);
            res.append("\n");
        }
        bf.close();
        return res.toString();
    }

    @FXML
    void translate(MouseEvent event) throws IOException {
        String in = input.getText();
        voice_from.setImage(Voice_Image);
        voice_to.setImage(Voice_Image);
        String res = (isEnglish) ? ConnectToGGAPI(in, "en", "vi") : ConnectToGGAPI(in, "vi", "en");
        output.setText(res);
    }

    @FXML
    void Voice_gg(MouseEvent event) throws IOException {
        ImageView clickedImageView = (ImageView) event.getSource();

        if (clickedImageView == voice_from) {
            playSound(input.getText(),isEnglish);
        } else if (clickedImageView == voice_to) {
            playSound(output.getText().trim(),!isEnglish);
        }
    }

    @FXML
    void speechToText(MouseEvent event) throws Exception {
        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    micro.setImage(Micro_On_Image);
                    input.setText("Recording...");
                    input.setEditable(false);
                });

                String pythonScriptPath = "src/main/resources/com/example/btl1_dictionary/Speech.py";
                String[] cmd = new String[2];
                cmd[0] = "python";
                cmd[1] = pythonScriptPath;

                System.out.println("ready to speech");
                Runtime rt = Runtime.getRuntime();
                Process pr = rt.exec(cmd);

                InputStreamReader reader = new InputStreamReader(pr.getInputStream());
                BufferedReader bfr = new BufferedReader(reader);
                String line = "";
                String result = "";

                while ((line = bfr.readLine()) != null) {
                    result += line;
                }
                bfr.close();

                String finalResult = result;
                Platform.runLater(() -> {
                    micro.setImage(Micro_Image);
                    input.setEditable(true);
                    if (finalResult.length() > 35) {
                        input.setText(finalResult.substring(35));
                    } else {
                        input.clear();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        output.clear();
        if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
            voice_from.setImage(null);
            voice_to.setImage(null);
        }
    }

    @FXML
    void switchLanguage(MouseEvent event) {
        if (isEnglish) {
            isEnglish = false;
            langFrom.setImage(Vie);
            langTo.setImage(Eng);
            upload.setVisible(false);
            micro.setVisible(false);
        }
        else {
            isEnglish = true;
            langFrom.setImage(Eng);
            langTo.setImage(Vie);
            upload.setVisible(true);
            micro.setVisible(true);
        }
    }

    @FXML
    void Clear(MouseEvent event) {
        input.clear();
        output.clear();
        voice_from.setImage(null);
        voice_to.setImage(null);
    }

    @FXML
    void UpLoadImage(MouseEvent event) {
        Ocr.setUp();
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg"));

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                Ocr ocr = new Ocr();
                ocr.startEngine("eng", Ocr.SPEED_FASTEST);
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                String result = ocr.recognize(bufferedImage, Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
                input.setText(result);
                ocr.stopEngine();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(" File chosen is not valid");
                alert.setHeaderText(null);
                alert.setContentText("Tệp được chọn không hợp lệ");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void Entered(MouseEvent event) {
        ImageView enteredImageView = (ImageView) event.getSource();

        if (enteredImageView == getSearch_Button()) {
            setSearch_Button(Search_Image);
            setGoogle_Button(null);
        } else if (enteredImageView == getHistory_Button()) {
            setHistory_Button(History_Image);
            setGoogle_Button(null);
        } else if (enteredImageView == getEdit_Button()) {
            setEdit_Button(Edit_Image);
            setGoogle_Button(null);
        } else if (enteredImageView == getSaved_Button()) {
            setSaved_Button(Saved_Image);
            setGoogle_Button(null);
        } else if (enteredImageView == getGame_Button()) {
            setGame_Button(Game_Image);
            setGoogle_Button(null);
        }
    }

    @Override
    public void Exited(MouseEvent event) {
        ImageView exitedImageView = (ImageView) event.getSource();

        if (exitedImageView == getSearch_Button()) {
            setSearch_Button(null);
            setGoogle_Button(Google_Image);
        } else if (exitedImageView == getHistory_Button()) {
            setHistory_Button(null);
            setGoogle_Button(Google_Image);
        } else if (exitedImageView == getEdit_Button()) {
            setEdit_Button(null);
            setGoogle_Button(Google_Image);
        } else if (exitedImageView == getSaved_Button()) {
            setSaved_Button(null);
            setGoogle_Button(Google_Image);
        } else if (exitedImageView == getGame_Button()) {
            setGame_Button(null);
            setGoogle_Button(Google_Image);
        }
    }
}
