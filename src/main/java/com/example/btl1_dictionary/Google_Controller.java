package com.example.btl1_dictionary;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
    private ImageView micro;

    @FXML
    private final Image Micro_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Micro_Button.png").toExternalForm());

    @FXML
    private final Image Micro_On_Image = new Image(getClass().getResource("/com/example/btl1_dictionary/Image/Micro_Button2.png").toExternalForm());

    private boolean checked = true;

    private boolean promptTextVisible = true;

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
        String res = (checked) ? ConnectToGGAPI(in, "en", "vi") : ConnectToGGAPI(in, "vi", "en");
        output.setText(res);
    }

    @FXML
    void Voice_gg(MouseEvent event) throws IOException {
        ImageView clickedImageView = (ImageView) event.getSource();

        if (clickedImageView == voice_from) {
            playSound(input.getText(),checked);
        } else if (clickedImageView == voice_to) {
            playSound(output.getText().trim(),!checked);
        }
    }

    @FXML
    void speechToText(MouseEvent event) throws Exception {
        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    micro.setImage(Micro_On_Image);
                    input.setPromptText("Please speak to the microphone");
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
                        input.setPromptText("Type here ...");
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    @FXML
    void onKeyTyped(KeyEvent event) {
        if (promptTextVisible) {
            input.setPromptText("");
            promptTextVisible = false;
        }

    }

    static void playSound(String in, boolean check) {
        try {
            String tmp = in.replace(" ", "%20");
            tmp = tmp.replace("\n", "%20");
            String apiUrl = (check) ? "https://api.voicerss.org/?key=331802f6088c4348b53f5cb3f553e3f3&hl=en-us&v=Chi&src="
                    : "https://api.voicerss.org/?key=331802f6088c4348b53f5cb3f553e3f3&hl=vi-vn&v=Odai&src=";
            apiUrl += tmp;
            URI uri = new URI(apiUrl);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            byte[] data = inputStream.readAllBytes();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 1000);

            clip.close();
            audioInputStream.close();
            byteArrayInputStream.close();
            inputStream.close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switchLanguage(MouseEvent event) {
        if (checked) {
            checked = false;
            langFrom.setImage(Vie);
            langTo.setImage(Eng);
        }
        else {
            checked = true;
            langFrom.setImage(Eng);
            langTo.setImage(Vie);
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
